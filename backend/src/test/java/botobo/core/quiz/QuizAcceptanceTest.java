package botobo.core.quiz;

import botobo.core.admin.dto.AdminAnswerRequest;
import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminCategoryRequest;
import botobo.core.auth.AuthAcceptanceTest;
import botobo.core.exception.ErrorResponse;
import botobo.core.quiz.dto.CategoryIdsRequest;
import botobo.core.quiz.dto.QuizResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static botobo.core.Fixture.ANSWER_REQUEST_1;
import static botobo.core.Fixture.ANSWER_REQUEST_10;
import static botobo.core.Fixture.ANSWER_REQUEST_11;
import static botobo.core.Fixture.ANSWER_REQUEST_12;
import static botobo.core.Fixture.ANSWER_REQUEST_13;
import static botobo.core.Fixture.ANSWER_REQUEST_14;
import static botobo.core.Fixture.ANSWER_REQUEST_15;
import static botobo.core.Fixture.ANSWER_REQUEST_2;
import static botobo.core.Fixture.ANSWER_REQUEST_3;
import static botobo.core.Fixture.ANSWER_REQUEST_4;
import static botobo.core.Fixture.ANSWER_REQUEST_5;
import static botobo.core.Fixture.ANSWER_REQUEST_6;
import static botobo.core.Fixture.ANSWER_REQUEST_7;
import static botobo.core.Fixture.ANSWER_REQUEST_8;
import static botobo.core.Fixture.ANSWER_REQUEST_9;
import static botobo.core.Fixture.CARD_REQUEST_1;
import static botobo.core.Fixture.CARD_REQUEST_10;
import static botobo.core.Fixture.CARD_REQUEST_11;
import static botobo.core.Fixture.CARD_REQUEST_12;
import static botobo.core.Fixture.CARD_REQUEST_13;
import static botobo.core.Fixture.CARD_REQUEST_14;
import static botobo.core.Fixture.CARD_REQUEST_15;
import static botobo.core.Fixture.CARD_REQUEST_2;
import static botobo.core.Fixture.CARD_REQUEST_3;
import static botobo.core.Fixture.CARD_REQUEST_4;
import static botobo.core.Fixture.CARD_REQUEST_5;
import static botobo.core.Fixture.CARD_REQUEST_6;
import static botobo.core.Fixture.CARD_REQUEST_7;
import static botobo.core.Fixture.CARD_REQUEST_8;
import static botobo.core.Fixture.CARD_REQUEST_9;
import static botobo.core.Fixture.CATEGORY_REQUEST_1;
import static botobo.core.Fixture.CATEGORY_REQUEST_2;
import static botobo.core.Fixture.CATEGORY_REQUEST_3;
import static botobo.core.TestUtils.extractId;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Quiz 인수 테스트")
public class QuizAcceptanceTest extends AuthAcceptanceTest {

    @BeforeEach
    void setFixture() {
        여러개_카테고리_생성_요청(Arrays.asList(CATEGORY_REQUEST_1, CATEGORY_REQUEST_2, CATEGORY_REQUEST_3));
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3, CARD_REQUEST_4,
                CARD_REQUEST_5, CARD_REQUEST_6, CARD_REQUEST_7, CARD_REQUEST_8, CARD_REQUEST_9, CARD_REQUEST_10,
                CARD_REQUEST_11, CARD_REQUEST_12, CARD_REQUEST_13, CARD_REQUEST_14, CARD_REQUEST_15));
        여러개_답변_생성_요청(Arrays.asList(ANSWER_REQUEST_1, ANSWER_REQUEST_2, ANSWER_REQUEST_3, ANSWER_REQUEST_4, ANSWER_REQUEST_5,
                ANSWER_REQUEST_6, ANSWER_REQUEST_7, ANSWER_REQUEST_8, ANSWER_REQUEST_9, ANSWER_REQUEST_10, ANSWER_REQUEST_11,
                ANSWER_REQUEST_12, ANSWER_REQUEST_13, ANSWER_REQUEST_14, ANSWER_REQUEST_15));
    }

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 성공")
    void createQuiz() {
        // given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        CategoryIdsRequest categoryIdsRequest =
                new CategoryIdsRequest(ids);

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(categoryIdsRequest)
                        .when().post("/quizzes")
                        .then().log().all()
                        .extract();

        // when
        final List<QuizResponse> quizResponses = response.body().jsonPath().getList(".", QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 실패, 카테고리 id가 없음")
    void createQuizWithEmptyCategoryIdList() {
        // given
        List<Long> ids = Collections.emptyList();
        CategoryIdsRequest categoryIdsRequest =
                new CategoryIdsRequest(ids);

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(categoryIdsRequest)
                        .when().post("/quizzes")
                        .then().log().all()
                        .extract();

        // when, then
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("퀴즈를 진행하려면 카테고리 아이디가 필요합니다.");
    }

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 실패, 존재하지 않는 ID")
    void createQuizWithNotExistId() {
        // given
        List<Long> ids = Arrays.asList(1000L, 1001L, 1002L);
        CategoryIdsRequest categoryIdsRequest =
                new CategoryIdsRequest(ids);

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(categoryIdsRequest)
                        .when().post("/quizzes")
                        .then().log().all()
                        .extract();

        // when, then
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(errorResponse.getMessage()).isEqualTo("해당 카테고리를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("퀴즈 생성 - 실패, 카테고리의 카드가 없을 경우")
    void createQuizWithNotExistCard() {
        // given
        final ExtractableResponse<Response> categoryResponse = 카테고리_생성_요청(new AdminCategoryRequest("4"));
        final Long id = extractId(categoryResponse);
        CategoryIdsRequest categoryIdsRequest = new CategoryIdsRequest(Arrays.asList(id));

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(categoryIdsRequest)
                        .when().post("/quizzes")
                        .then().log().all()
                        .extract();

        // when, then
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(errorResponse.getMessage()).isEqualTo("카드가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("퀴즈 생성 - 실패, 카테고리의 카드의 답변이 없을 경우")
    void createQuizWithNotExistAnswer() {
        // given
        final ExtractableResponse<Response> categoryResponse = 카테고리_생성_요청(new AdminCategoryRequest("4"));
        final Long id = extractId(categoryResponse);
        CategoryIdsRequest categoryIdsRequest = new CategoryIdsRequest(Arrays.asList(id));
        카드_생성_요청(new AdminCardRequest("question", id));

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(categoryIdsRequest)
                        .when().post("/quizzes")
                        .then().log().all()
                        .extract();

        // when, then
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(errorResponse.getMessage()).isEqualTo("답변이 존재하지 않습니다.");
    }

    public ExtractableResponse<Response> 카테고리_생성_요청(AdminCategoryRequest adminCategoryRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(로그인되어_있음().getAccessToken())
                .body(adminCategoryRequest)
                .when().post("/admin/categories")
                .then().log().all()
                .extract();
    }

    public void 여러개_카테고리_생성_요청(List<AdminCategoryRequest> requests) {
        for (AdminCategoryRequest request : requests) {
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().oauth2(로그인되어_있음().getAccessToken())
                    .body(request)
                    .when().post("/admin/categories")
                    .then().log().all()
                    .extract();
        }
    }

    public ExtractableResponse<Response> 카드_생성_요청(AdminCardRequest adminCardRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(로그인되어_있음().getAccessToken())
                .body(adminCardRequest)
                .when().post("/admin/cards")
                .then().log().all()
                .extract();
    }

    public void 여러개_카드_생성_요청(List<AdminCardRequest> requests) {
        for (AdminCardRequest request : requests) {
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().oauth2(로그인되어_있음().getAccessToken())
                    .body(request)
                    .when().post("/admin/cards")
                    .then().log().all()
                    .extract();
        }
    }

    public void 여러개_답변_생성_요청(List<AdminAnswerRequest> requests) {
        for (AdminAnswerRequest request : requests)
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().oauth2(로그인되어_있음().getAccessToken())
                    .body(request)
                    .when().post("/admin/answers")
                    .then().log().all()
                    .extract();
    }
}
