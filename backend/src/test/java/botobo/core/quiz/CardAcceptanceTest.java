package botobo.core.quiz;

import botobo.core.DomainAcceptanceTest;
import botobo.core.common.exception.ErrorResponse;
import botobo.core.quiz.dto.CardRequest;
import botobo.core.quiz.dto.CardResponse;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static botobo.core.Fixture.CARD_REQUEST_1;
import static botobo.core.Fixture.CARD_REQUEST_2;
import static botobo.core.Fixture.CARD_REQUEST_3;
import static botobo.core.Fixture.WORKBOOK_REQUEST_1;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("카드 인수 테스트")
public class CardAcceptanceTest extends DomainAcceptanceTest {

    @BeforeEach
    void setFixture() {
        문제집_생성_요청(WORKBOOK_REQUEST_1);
        여러개_카드_생성_요청(Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3));
    }

    @Test
    @DisplayName("카드 생성 - 성공")
    void createCard() {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(1L)
                .build();

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(cardRequest)
                        .when().post("/api/cards")
                        .then().log().all()
                        .extract();

        // then
        CardResponse cardResponse = response.as(CardResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(cardResponse).extracting("id").isNotNull();
        assertThat(cardResponse).extracting("question").isEqualTo(cardRequest.getQuestion());
        assertThat(cardResponse).extracting("answer").isEqualTo(cardRequest.getAnswer());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("카드 생성 - 실패, 유효하지 않은 question")
    void createCardWithInvalidQuestion(String question) {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question(question)
                .answer("answer")
                .workbookId(1L)
                .build();

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(cardRequest)
                        .when().post("/api/cards")
                        .then().log().all()
                        .extract();

        // then
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse).extracting("message").isEqualTo("질문은 필수 입력값입니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("카드 생성 - 실패, 유효하지 않은 answer")
    void createCardWithInvalidAnswer(String answer) {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer(answer)
                .workbookId(1L)
                .build();

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(cardRequest)
                        .when().post("/api/cards")
                        .then().log().all()
                        .extract();

        // then
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse).extracting("message").isEqualTo("답변은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("카드 생성 - 실패, 유효하지 않은 문제집 id")
    void createCardWithInvalidWorkbookId() {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(null)
                .build();

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(cardRequest)
                        .when().post("/api/cards")
                        .then().log().all()
                        .extract();

        // then
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse).extracting("message").isEqualTo("카드가 포함될 문제집 아이디는 필수 입력값입니다.");

    }

    @Test
    @DisplayName("카드 생성 - 실패, 존재하지 않는 문제집 id")
    void createCardWithNoneExistingWorkbookId() {
        // given
        Long noneExisingWorkbookId = 10000L;
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(noneExisingWorkbookId)
                .build();

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(cardRequest)
                        .when().post("/api/cards")
                        .then().log().all()
                        .extract();

        // then
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(errorResponse).extracting("message").isEqualTo("해당 문제집을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 성공")
    void selectNextQuizCards() {
        // given
        NextQuizCardsRequest nextQuizCardsRequest = NextQuizCardsRequest.builder()
                .cardIds(List.of(1L, 2L, 3L))
                .build();

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(nextQuizCardsRequest)
                        .when().put("/api/cards/next-quiz")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 실패, 요청으로 null 들어오는 경우")
    void selectNextQuizCardsWithNullList() {
        // given
        NextQuizCardsRequest nextQuizCardsRequest = NextQuizCardsRequest.builder()
                .cardIds(null)
                .build();

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(로그인되어_있음().getAccessToken())
                        .body(nextQuizCardsRequest)
                        .when().put("/api/cards/next-quiz")
                        .then().log().all()
                        .extract();

        // then
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse).extracting("message").isEqualTo("유효하지 않은 또 보기 카드 등록 요청입니다.");
    }
}
