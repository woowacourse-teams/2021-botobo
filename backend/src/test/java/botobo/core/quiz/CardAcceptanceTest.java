package botobo.core.quiz;

import botobo.core.DomainAcceptanceTest;
import botobo.core.common.exception.ErrorResponse;
import botobo.core.quiz.dto.CardRequest;
import botobo.core.quiz.dto.CardResponse;
import botobo.core.quiz.dto.CardUpdateRequest;
import botobo.core.quiz.dto.CardUpdateResponse;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import botobo.core.utils.RequestBuilder.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static botobo.core.utils.Fixture.CARD_REQUEST_1;
import static botobo.core.utils.Fixture.CARD_REQUEST_2;
import static botobo.core.utils.Fixture.CARD_REQUEST_3;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_1;
import static botobo.core.utils.TestUtils.longStringGenerator;
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
        final HttpResponse response = request()
                .post("/api/cards", cardRequest)
                .auth()
                .build();

        // then
        CardResponse cardResponse = response.convertBody(CardResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
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
        final HttpResponse response = request()
                .post("/api/cards", cardRequest)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("질문은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("카드 생성 - 실패, 255자를 넘긴 question")
    void createCardWithLongQuestion() {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question(longStringGenerator(266))
                .answer("answer")
                .workbookId(1L)
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/cards", cardRequest)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("질문은 최대 255자까지 입력 가능합니다.");
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
        final HttpResponse response = request()
                .post("/api/cards", cardRequest)
                .auth()
                .build();
        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("답변은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("카드 생성 - 실패, 255자를 넘긴 answer")
    void createCardWithLongAnswer() {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer(longStringGenerator(266))
                .workbookId(1L)
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/cards", cardRequest)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("답변은 최대 255자까지 입력 가능합니다.");
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
        final HttpResponse response = request()
                .post("/api/cards", cardRequest)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
        final HttpResponse response = request()
                .post("/api/cards", cardRequest)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse).extracting("message").isEqualTo("해당 문제집을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("카드 수정 - 성공")
    void updateCard() {
        // given
        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question("question")
                .answer("answer")
                .bookmark(true)
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/1", cardUpdateRequest)
                .auth()
                .build();

        // then
        CardUpdateResponse cardUpdateResponse = response.convertBody(CardUpdateResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cardUpdateResponse).extracting("question").isEqualTo(cardUpdateRequest.getQuestion());
        assertThat(cardUpdateResponse).extracting("answer").isEqualTo(cardUpdateRequest.getAnswer());
        assertThat(cardUpdateResponse).extracting("bookmark").isEqualTo(cardUpdateRequest.getBookmark());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("카드 수정 - 실패, 유효하지 않은 question")
    void updateCardWithInvalidQuestion(String question) {
        // given
        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question(question)
                .answer("answer")
                .bookmark(true)
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/1", cardUpdateRequest)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("카드를 업데이트하기 위해서는 질문이 필요합니다.");
    }

    @Test
    @DisplayName("카드 수정 - 실패, 255자를 넘긴 question")
    void updateCardWithLongQuestion() {
        // given
        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question(longStringGenerator(266))
                .answer("answer")
                .bookmark(true)
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/1", cardUpdateRequest)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("질문은 최대 255자까지 입력 가능합니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("카드 수정 - 실패, 유효하지 않은 answer")
    void updateCardWithInvalidAnswer(String answer) {
        // given
        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question("question")
                .answer(answer)
                .bookmark(true)
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/1", cardUpdateRequest)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("카드를 업데이트하기 위해서는 답변이 필요합니다.");
    }

    @Test
    @DisplayName("카드 수정 - 실패, 255자를 넘긴 answer")
    void updateCardWithLongAnswer() {
        // given
        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question("question")
                .answer(longStringGenerator(266))
                .bookmark(true)
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/1", cardUpdateRequest)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("답변은 최대 255자까지 입력 가능합니다.");
    }

    @Test
    @DisplayName("카드 삭제 - 성공")
    void deleteCard() {
        // given
        Long cardId = 1L;

        // when
        final HttpResponse response = request()
                .delete("/api/cards/" + cardId)
                .auth()
                .build();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 성공")
    void selectNextQuizCards() {
        // given
        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(List.of(1L, 2L, 3L))
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/next-quiz", request)
                .auth()
                .build();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 실패, 요청으로 null 들어오는 경우")
    void selectNextQuizCardsWithNullList() {
        // given
        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(null)
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/next-quiz", request)
                .auth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("유효하지 않은 또 보기 카드 등록 요청입니다.");
    }
}
