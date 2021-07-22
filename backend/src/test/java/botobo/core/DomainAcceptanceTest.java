package botobo.core;

import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminWorkbookRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class DomainAcceptanceTest extends AcceptanceTest {

    public ExtractableResponse<Response> 문제집_생성_요청(AdminWorkbookRequest adminWorkbookRequest) {
        return request()
                .post("/api/admin/workbooks", adminWorkbookRequest)
                .auth()
                .build()
                .extractableResponse();
    }

    public void 여러개_문제집_생성_요청(List<AdminWorkbookRequest> adminRequests) {
        for (AdminWorkbookRequest adminRequest : adminRequests) {
            request()
                    .post("/api/admin/workbooks", adminRequest)
                    .auth()
                    .build()
                    .extractableResponse();
        }
    }

    public void 여러개_카드_생성_요청(List<AdminCardRequest> adminCardRequests) {
        for (AdminCardRequest adminCardRequest : adminCardRequests) {
            request()
                    .post("/api/admin/cards", adminCardRequest)
                    .auth()
                    .build()
                    .extractableResponse();
        }
    }
}
