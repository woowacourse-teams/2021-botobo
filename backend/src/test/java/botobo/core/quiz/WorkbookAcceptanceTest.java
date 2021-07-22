package botobo.core.quiz;

import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminWorkbookRequest;
import botobo.core.auth.AuthAcceptanceTest;
import botobo.core.quiz.dto.WorkbookCardResponse;
import botobo.core.quiz.dto.WorkbookResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static botobo.core.Fixture.CARD_REQUEST_1;
import static botobo.core.Fixture.CARD_REQUEST_2;
import static botobo.core.Fixture.CARD_REQUEST_3;
import static botobo.core.Fixture.WORKBOOK_REQUEST_1;
import static botobo.core.Fixture.WORKBOOK_REQUEST_2;
import static botobo.core.Fixture.WORKBOOK_REQUEST_3;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Workbook 인수 테스트")
public class WorkbookAcceptanceTest extends AuthAcceptanceTest {

    @BeforeEach
    void setFixture() {
        여러개_문제집_생성_요청(Arrays.asList(WORKBOOK_REQUEST_1, WORKBOOK_REQUEST_2, WORKBOOK_REQUEST_3));
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3));
    }

    @Test
    @DisplayName("문제집 전체 조회 - 성공")
    void findAllCategories() {
        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/api/workbooks")
                        .then().log().all()
                        .extract();

        // then
        final List<WorkbookResponse> workbookResponse = response.body().jsonPath().getList(".", WorkbookResponse.class);
        assertThat(workbookResponse.get(0).getCardCount()).isEqualTo(3);
        assertThat(workbookResponse.get(1).getCardCount()).isZero();
        assertThat(workbookResponse.get(2).getCardCount()).isZero();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(workbookResponse.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 (카드 존재) - 성공")
    void findCategoryCardsById() {
        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/api/workbooks/{id}/cards", 1L)
                        .then().log().all()
                        .extract();
        // then
        final WorkbookCardResponse workbookCardResponse = response.as(WorkbookCardResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(WORKBOOK_REQUEST_1.getName());
        assertThat(workbookCardResponse.getCards()).hasSize(3);
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 (카드 0개) - 성공")
    void findWorkbookCardsByIdWithNotExistsCard() {
        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/api/workbooks/{id}/cards", 2L)
                        .then().log().all()
                        .extract();

        // then
        final WorkbookCardResponse workbookCardResponse = response.as(WorkbookCardResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(WORKBOOK_REQUEST_2.getName());
        assertThat(workbookCardResponse.getCards()).isEmpty();
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
