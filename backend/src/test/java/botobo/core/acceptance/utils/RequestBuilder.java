package botobo.core.acceptance.utils;

import botobo.core.exception.common.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RequestBuilder {
    public RequestBuilder() {
    }

    public HttpFunction build() {
        return new HttpFunction();
    }

    interface RestAssuredRequest {
        ValidatableResponse action(RequestSpecification specification);
    }

    public static class HttpFunction {
        public Options get(String path, Object... params) {
            return new Options(new GetRequest(path, params));
        }

        public <T> Options post(String path, T body, Object... params) {
            return new Options(new PostRequest<>(path, body, params));
        }

        public <T> Options put(String path, T body, Object... params) {
            return new Options(new PutRequest<>(path, body, params));
        }

        public Options putWithoutBody(String path, Object... params) {
            return new Options(new PutRequest<>(path, params));
        }

        public Options delete(String path, Object... params) {
            return new Options(new DeleteRequest(path, params));
        }
    }

    public static class Options {
        private final List<Map.Entry<String, Object>> queryParams;
        private final List<Map.Entry<String, String>> cookies;
        private final RestAssuredRequest request;
        private String accessToken;
        private boolean loginFlag;
        private boolean logFlag;

        public Options(RestAssuredRequest request) {
            this.request = request;
            this.loginFlag = false;
            this.logFlag = false;
            this.queryParams = new ArrayList<>();
            this.cookies = new ArrayList<>();
        }

        public Options log() {
            this.logFlag = true;
            return this;
        }

        public Options auth(String token) {
            this.loginFlag = true;
            this.accessToken = token;
            return this;
        }

        public Options queryParam(String key, String value) {
            this.queryParams.add(new AbstractMap.SimpleEntry<>(key, value));
            return this;
        }

        public Options queryParams(Map<String, Object> parameters) {
            this.queryParams.addAll(parameters.entrySet());
            return this;
        }

        public Options cookie(String name, String value) {
            this.cookies.add(new AbstractMap.SimpleEntry<>(name, value));
            return this;
        }

        public HttpResponse build() {
            RequestSpecification requestSpecification = RestAssured.given();
            if (loginFlag) {
                requestSpecification = addAuthHeader(requestSpecification, accessToken);
            }
            for (Map.Entry<String, Object> param : queryParams) {
                requestSpecification = addParams(requestSpecification, param);
            }
            for (Map.Entry<String, String> cookie : cookies) {
                requestSpecification = addCookies(requestSpecification, cookie);
            }
            ValidatableResponse response = request.action(requestSpecification);
            if (logFlag) {
                response = response.log().all();
            }
            return new HttpResponse(response.extract());
        }

        private RequestSpecification addAuthHeader(RequestSpecification requestSpecification, String token) {
            return requestSpecification.header("Authorization", "Bearer " + token);
        }

        private RequestSpecification addParams(RequestSpecification requestSpecification, Map.Entry<String, Object> param) {
            return requestSpecification.queryParam(param.getKey(), param.getValue());
        }

        private RequestSpecification addCookies(RequestSpecification requestSpecification, Map.Entry<String, String> cookie) {
            return requestSpecification.cookie(cookie.getKey(), cookie.getValue());
        }
    }

    public static class HttpResponse {
        private final ExtractableResponse<Response> response;

        public HttpResponse(ExtractableResponse<Response> response) {
            this.response = response;
        }

        public <T> T convertBody(Class<T> targetClass) {
            return response.body().as(targetClass);
        }

        public <T> List<T> convertBodyToList(Class<T> target) {
            return response.body().jsonPath().getList(".", target);
        }

        public HttpStatus statusCode() {
            return HttpStatus.valueOf(response.statusCode());
        }

        public ErrorResponse convertToErrorResponse() {
            return response.as(ErrorResponse.class);
        }

        public ExtractableResponse<Response> extract() {
            return response;
        }

        public String header(String name) {
            return response.header(name);
        }

        public Cookie detailedCookie(String name) {
            return response.detailedCookie(name);
        }
    }

    private static class GetRequest implements RestAssuredRequest {
        private final String path;
        private final Object[] params;

        public GetRequest(String path, Object[] params) {
            this.path = path;
            this.params = params;
        }

        @Override
        public ValidatableResponse action(RequestSpecification specification) {
            return specification.get(path, params)
                    .then();
        }
    }

    private static class PostRequest<T> implements RestAssuredRequest {
        private final String path;
        private final T body;
        private final Object[] params;

        public PostRequest(String path, T body, Object[] params) {
            this.path = path;
            this.body = body;
            this.params = params;
        }

        @Override
        public ValidatableResponse action(RequestSpecification specification) {
            if (Objects.isNull(body)) {
                return specification
                        .post(path, params)
                        .then();
            }
            return specification.body(body)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .post(path, params)
                    .then();
        }
    }

    private static class PutRequest<T> implements RestAssuredRequest {
        private final String path;
        private final T body;
        private final Object[] params;

        public PutRequest(String path, Object[] params) {
            this(path, null, params);
        }

        public PutRequest(String path, T body, Object[] params) {
            this.path = path;
            this.body = body;
            this.params = params;
        }

        @Override
        public ValidatableResponse action(RequestSpecification specification) {
            if (Objects.nonNull(body)) {
                specification.body(body);
            }
            return specification
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .put(path, params)
                    .then();
        }
    }

    private static class DeleteRequest implements RestAssuredRequest {
        private final String path;
        private final Object[] params;

        public DeleteRequest(String path, Object[] params) {
            this.path = path;
            this.params = params;
        }

        @Override
        public ValidatableResponse action(RequestSpecification specification) {
            return specification
                    .delete(path, params)
                    .then();
        }
    }
}

