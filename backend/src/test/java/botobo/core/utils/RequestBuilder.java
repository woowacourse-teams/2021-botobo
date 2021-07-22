package botobo.core.utils;

import botobo.core.common.exception.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

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

        public <T> Options post(String path, T body) {
            return new Options(new PostRequest<>(path, body));
        }

        public <T> Options put(String path, T body) {
            return new Options(new PutRequest<>(path, body));
        }

        public Options delete(String path, Object... params) {
            return new Options(new DeleteRequest(path, params));
        }
    }

    public static class Options {
        private final RestAssuredRequest request;
        private RequestSpecification requestSpecification;
        private boolean loginFlag;

        public Options(RestAssuredRequest request) {
            this.request = request;
            this.requestSpecification = RestAssured.given().log().all();
            this.loginFlag = false;
        }

        public Options auth() {
            this.loginFlag = true;
            return this;
        }

        public Options auth(String token) {
            RequestBuilder.accessToken = token;
            this.loginFlag = true;
            return this;
        }

        public HttpResponse build() {
            if (loginFlag) {
                requestSpecification = requestSpecification.header("Authorization", "Bearer " + accessToken);
            }
            ValidatableResponse response = request.doAction(requestSpecification).log().all();
            return new HttpResponse(response.extract());
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

        public ErrorResponse errorResponse() {
            return response.as(ErrorResponse.class);
        }

        public ExtractableResponse<Response> extractableResponse() {
            return response;
        }
    }

    interface RestAssuredRequest {
        ValidatableResponse doAction(RequestSpecification specification);
    }

    private static class GetRequest implements RestAssuredRequest {
        private final String path;
        private final Object[] params;

        public GetRequest(String path, Object[] params) {
            this.path = path;
            this.params = params;
        }

        @Override
        public ValidatableResponse doAction(RequestSpecification specification) {
            return specification.get(path, params)
                    .then();
        }
    }

    private static class PostRequest<T> implements RestAssuredRequest {
        private final String path;
        private final T body;

        public PostRequest(String path, T body) {
            this.path = path;
            this.body = body;
        }

        @Override
        public ValidatableResponse doAction(RequestSpecification specification) {
            return specification.body(body)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .post(path)
                    .then();
        }
    }

    private static class PutRequest<T> implements RestAssuredRequest {
        private final String path;
        private final T body;

        public PutRequest(String path, T body) {
            this.path = path;
            this.body = body;
        }

        @Override
        public ValidatableResponse doAction(RequestSpecification specification) {
            return specification.body(body)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .put(path)
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
        public ValidatableResponse doAction(RequestSpecification specification) {
            return specification
                    .delete(path, params)
                    .then();
        }
    }
}

