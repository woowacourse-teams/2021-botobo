package botobo.core;

import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminWorkbookRequest;
import botobo.core.auth.AuthAcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.List;

public class DomainAcceptanceTest extends AuthAcceptanceTest {

    public ExtractableResponse<Response> 문제집_생성_요청(AdminWorkbookRequest adminWorkbookRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(로그인되어_있음().getAccessToken())
                .body(adminWorkbookRequest)
                .when().post("/api/admin/workbooks")
                .then().log().all()
                .extract();
    }

    public void 여러개_문제집_생성_요청(List<AdminWorkbookRequest> requests) {
        for (AdminWorkbookRequest request : requests) {
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().oauth2(로그인되어_있음().getAccessToken())
                    .body(request)
                    .when().post("/api/admin/workbooks")
                    .then().log().all()
                    .extract();
        }
    }

    public void 여러개_카드_생성_요청(List<AdminCardRequest> requests) {
        for (AdminCardRequest request : requests) {
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().oauth2(로그인되어_있음().getAccessToken())
                    .body(request)
                    .when().post("/api/admin/cards")
                    .then().log().all()
                    .extract();
        }
    }
}
