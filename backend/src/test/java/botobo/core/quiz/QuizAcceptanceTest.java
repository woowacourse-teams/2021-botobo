package botobo.core.quiz;

import botobo.core.DomainAcceptanceTest;
import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminWorkbookRequest;
import botobo.core.auth.AuthAcceptanceTest;
import botobo.core.common.exception.ErrorResponse;
import botobo.core.quiz.dto.QuizRequest;
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
import static botobo.core.Fixture.WORKBOOK_REQUEST_1;
import static botobo.core.Fixture.WORKBOOK_REQUEST_2;
import static botobo.core.Fixture.WORKBOOK_REQUEST_3;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Quiz 인수 테스트")
public class QuizAcceptanceTest extends DomainAcceptanceTest {

    @BeforeEach
    void setFixture() {

        여러개_문제집_생성_요청(Arrays.asList(WORKBOOK_REQUEST_1, WORKBOOK_REQUEST_2, WORKBOOK_REQUEST_3));
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3, CARD_REQUEST_4,
                CARD_REQUEST_5, CARD_REQUEST_6, CARD_REQUEST_7, CARD_REQUEST_8, CARD_REQUEST_9, CARD_REQUEST_10,
                CARD_REQUEST_11, CARD_REQUEST_12, CARD_REQUEST_13, CARD_REQUEST_14, CARD_REQUEST_15));
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 퀴즈 생성 - 성공")
    void createQuiz() {
        // given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        QuizRequest quizRequest =
                new QuizRequest(ids);

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(quizRequest)
                        .when().post("/api/quizzes")
                        .then().log().all()
                        .extract();

        // when
        final List<QuizResponse> quizResponses = response.body().jsonPath().getList(".", QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 퀴즈 생성 - 실패, 문제집 id가 없음")
    void createQuizWithEmptyCategoryIdList() {
        // given
        List<Long> ids = Collections.emptyList();
        QuizRequest quizRequest =
                new QuizRequest(ids);

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(quizRequest)
                        .when().post("/api/quizzes")
                        .then().log().all()
                        .extract();

        // when, then
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("퀴즈를 진행하려면 문제집 아이디가 필요합니다.");
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 퀴즈 생성 - 실패, 존재하지 않는 ID")
    void createQuizWithNotExistId() {
        // given
        List<Long> ids = Arrays.asList(1000L, 1001L, 1002L);
        QuizRequest quizRequest =
                new QuizRequest(ids);

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(quizRequest)
                        .when().post("/api/quizzes")
                        .then().log().all()
                        .extract();

        // when, then
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(errorResponse.getMessage()).isEqualTo("해당 문제집을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("비회원용 퀴즈 생성 - 성공")
    void createQuizForGuest() {
        // given
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().get("/api/quizzes/guest")
                        .then().log().all()
                        .extract();

        // when
        final List<QuizResponse> quizResponses = response.body().jsonPath().getList(".", QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("비회원용 퀴즈 생성을 여러번 요청해도 동일한 퀴즈를 제공한다. - 성공")
    void createQuizForGuestMultipleRequest() {
        // given
        final ExtractableResponse<Response> firstResponse =
                RestAssured.given().log().all()
                        .when().get("/api/quizzes/guest")
                        .then().log().all()
                        .extract();
        final List<QuizResponse> firstQuizResponses = firstResponse.body().jsonPath().getList(".", QuizResponse.class);

        final ExtractableResponse<Response> secondResponse =
                RestAssured.given().log().all()
                        .when().get("/api/quizzes/guest")
                        .then().log().all()
                        .extract();
        final List<QuizResponse> secondQuizResponses = secondResponse.body().jsonPath().getList(".", QuizResponse.class);

        // when - then
        assertThat(firstResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(firstQuizResponses.size()).isEqualTo(10);

        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(secondQuizResponses.size()).isEqualTo(10);
    }
}
