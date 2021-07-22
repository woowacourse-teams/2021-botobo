package botobo.core.utils;

import botobo.core.exception.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

import java.util.List;

public class RequestBuilder {
    private String accessToken;

    public RequestBuilder(String accessToken) {
        this.accessToken = accessToken;
    }

    public HttpFunction build() {
        return new HttpFunction();
    }

    public class HttpFunction {
        public Details get(String path, Object... params) {
            return new Details(new GetRequest(path, params));
        }
    }

    public class Details {
        private final RestAssuredRequest request;
        private boolean loginFlag;

        public Details(RestAssuredRequest request) {
            this.request = request;
            this.loginFlag = false;
        }

        public Details auth() {
            this.loginFlag = true;
            return this;
        }

        public HttpResponse build() {
            RequestSpecification requestSpecification = RestAssured.given().log().all();
            if (loginFlag) {
                requestSpecification = requestSpecification.header("Authorization", "Bearer " + accessToken);
            }
            ValidatableResponse response = request.doAction(requestSpecification).log().all();
            return new HttpResponse(response.extract());
        }

    }

    public class HttpResponse {
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
}

