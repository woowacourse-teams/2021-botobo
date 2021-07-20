package botobo.core.quiz;

import botobo.core.AcceptanceTest;
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
import static botobo.core.admin.AdminAcceptanceTest.여러개_문제집_생성_요청;
import static botobo.core.admin.AdminAcceptanceTest.여러개_카드_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Workbook 인수 테스트")
public class WorkbookAcceptanceTest extends AcceptanceTest {

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
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/workbooks")
                        .then().log().all()
                        .extract();

        // then
        final List<WorkbookResponse> workbookResponse = response.body().jsonPath().getList(".", WorkbookResponse.class);
        assertThat(workbookResponse.get(0).getCardCount()).isZero();
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
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/workbooks/{id}/cards", 1L)
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
    void findCategoryCardsByIdWithNotExistsCard() {
        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/workbooks/{id}/cards", 2L)
                        .then().log().all()
                        .extract();

        // then
        final WorkbookCardResponse workbookCardResponse = response.as(WorkbookCardResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(workbookCardResponse.getWorkbookName()).isEqualTo(WORKBOOK_REQUEST_2.getName());
        assertThat(workbookCardResponse.getCards()).isEmpty();
    }
}
