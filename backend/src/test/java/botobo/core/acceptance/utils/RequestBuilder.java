package botobo.core.acceptance.utils;

import botobo.core.exception.common.ErrorResponse;
import io.restassured.RestAssured;
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
    private static String accessToken;

    public RequestBuilder(String accessToken) {
        RequestBuilder.accessToken = accessToken;
    }

    public HttpFunction build() {
        return new HttpFunction();
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

        public Options delete(String path, Object... params) {
            return new Options(new DeleteRequest(path, params));
        }
    }

    public static class Options {
        private final List<Map.Entry<String, String>> queryParams;
        private final RestAssuredRequest request;
        private String customAccessToken;
        private boolean loginFlag;
        private boolean logFlag;

        public Options(RestAssuredRequest request) {
            this.request = request;
            this.loginFlag = false;
            this.logFlag = false;
            this.queryParams = new ArrayList<>();
        }

        public Options log() {
            this.logFlag = true;
            return this;
        }

        public Options failAuth() {
            this.loginFlag = true;
            return this;
        }

        public Options auth(String token) {
            this.loginFlag = true;
            this.customAccessToken = token;
            return this;
        }

        public Options queryParam(String key, String value) {
            this.queryParams.add(new AbstractMap.SimpleEntry<>(key, value));
            return this;
        }

        public Options queryParams(Map<String, String> parameters) {
            this.queryParams.addAll(parameters.entrySet());
            return this;
        }

        public HttpResponse build() {
            RequestSpecification requestSpecification = RestAssured.given();
            if (loginFlag) {
                requestSpecification = addAuthHeader(requestSpecification, getToken());
            }
            for (Map.Entry<String, String> param : queryParams) {
                requestSpecification = addParams(requestSpecification, param);
            }
            ValidatableResponse response = request.action(requestSpecification);
            if (logFlag) {
                response = response.log().all();
            }
            return new HttpResponse(response.extract());
        }

        private String getToken() {
            if (Objects.isNull(customAccessToken)) {
                return accessToken;
            }
            final String token = customAccessToken;
            customAccessToken = null;
            return token;
        }

        private RequestSpecification addParams(RequestSpecification requestSpecification, Map.Entry<String, String> param) {
            return requestSpecification.queryParam(param.getKey(), param.getValue());
        }

        private RequestSpecification addAuthHeader(RequestSpecification requestSpecification, String token) {
            return requestSpecification.header("Authorization", "Bearer " + token);
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
    }

    interface RestAssuredRequest {
        ValidatableResponse action(RequestSpecification specification);
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

        public PutRequest(String path, T body, Object[] params) {
            this.path = path;
            this.body = body;
            this.params = params;
        }

        @Override
        public ValidatableResponse action(RequestSpecification specification) {
            return specification.body(body)
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

