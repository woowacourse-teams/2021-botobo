package botobo.core.acceptance.card;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.card.CardUpdateRequest;
import botobo.core.dto.card.CardUpdateResponse;
import botobo.core.dto.card.NextQuizCardsRequest;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.List;

import static botobo.core.utils.Fixture.ditto;
import static botobo.core.utils.Fixture.joanne;
import static botobo.core.utils.Fixture.kyle;
import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("카드 인수 테스트")
public class CardAcceptanceTest extends DomainAcceptanceTest {

    private String joanneToken;

    private Long workbookId;

    @BeforeEach
    void setFixture() {
        joanneToken = 소셜_로그인되어_있음(joanne, SocialType.GOOGLE);
        workbookId = 유저_태그_포함_문제집_등록되어_있음("문제집", true, joanneToken).getId();
    }

    @Test
    @DisplayName("카드 생성 - 성공")
    void createCard() {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(workbookId)
                .build();

        // when
        final HttpResponse response = 유저_카드_생성_요청(cardRequest, joanneToken);

        // then
        CardResponse cardResponse = response.convertBody(CardResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(cardResponse).extracting("id").isNotNull();
        assertThat(cardResponse).extracting("question").isEqualTo(cardRequest.getQuestion());
        assertThat(cardResponse).extracting("answer").isEqualTo(cardRequest.getAnswer());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("카드 생성 - 실패, 유효하지 않은 question")
    void createCardWithInvalidQuestion(String question) {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question(question)
                .answer("answer")
                .workbookId(workbookId)
                .build();

        // when
        final HttpResponse response = 유저_카드_생성_요청(cardRequest, joanneToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("질문은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("카드 생성 - 실패, 2000자를 넘긴 question")
    void createCardWithLongQuestion() {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question(stringGenerator(2001))
                .answer("answer")
                .workbookId(workbookId)
                .build();

        // when
        final HttpResponse response = 유저_카드_생성_요청(cardRequest, joanneToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("질문은 최대 2000자까지 입력 가능합니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("카드 생성 - 실패, 유효하지 않은 answer")
    void createCardWithInvalidAnswer(String answer) {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer(answer)
                .workbookId(workbookId)
                .build();

        // when
        final HttpResponse response = 유저_카드_생성_요청(cardRequest, joanneToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("답변은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("카드 생성 - 실패, 2000자를 넘긴 answer")
    void createCardWithLongAnswer() {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer(stringGenerator(2001))
                .workbookId(workbookId)
                .build();

        // when
        final HttpResponse response = 유저_카드_생성_요청(cardRequest, joanneToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("답변은 최대 2000자까지 입력 가능합니다.");
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
        final HttpResponse response = 유저_카드_생성_요청(cardRequest, joanneToken);

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
        final HttpResponse response = 유저_카드_생성_요청(cardRequest, joanneToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse).extracting("message").isEqualTo("해당 문제집을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("카드 생성 - 실패, 유저가 존재하지 않음.")
    void createCardWithNotExistUser() {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(workbookId)
                .build();

        // when
        final HttpResponse response = request()
                .post("/api/cards", cardRequest)
                .failAuth() // 100L
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse).extracting("message").isEqualTo("해당 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("카드 생성 - 실패, 문제집의 유저와 일치하지 않음.")
    void createCardWithNotSameUser() {
        // given
        CardRequest cardRequest = CardRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(workbookId)
                .build();
        final String anotherToken = 소셜_로그인되어_있음(kyle, SocialType.GOOGLE);

        // when
        final HttpResponse response = 유저_카드_생성_요청(cardRequest, anotherToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse).extracting("message").isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @Test
    @DisplayName("카드 수정 - 성공")
    void updateCard() {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);

        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(workbookId)
                .encounterCount(0)
                .bookmark(true)
                .nextQuiz(true)
                .build();

        // when
        final HttpResponse response = 유저_카드_수정_요청(cardUpdateRequest, cardResponse, joanneToken);

        // then
        CardUpdateResponse cardUpdateResponse = response.convertBody(CardUpdateResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cardUpdateResponse).extracting("question").isEqualTo(cardUpdateRequest.getQuestion());
        assertThat(cardUpdateResponse).extracting("answer").isEqualTo(cardUpdateRequest.getAnswer());
        assertThat(cardUpdateResponse).extracting("workbookId").isEqualTo(cardUpdateRequest.getWorkbookId());
        assertThat(cardUpdateResponse).extracting("encounterCount").isEqualTo(cardUpdateRequest.getEncounterCount());
        assertThat(cardUpdateResponse).extracting("bookmark").isEqualTo(cardUpdateRequest.getBookmark());
        assertThat(cardUpdateResponse).extracting("nextQuiz").isEqualTo(cardUpdateRequest.getNextQuiz());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("카드 수정 - 실패, 유효하지 않은 question")
    void updateCardWithInvalidQuestion(String question) {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);

        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question(question)
                .answer("answer")
                .workbookId(workbookId)
                .encounterCount(0)
                .bookmark(true)
                .nextQuiz(true)
                .build();

        // when
        final HttpResponse response = 유저_카드_수정_요청(cardUpdateRequest, cardResponse, joanneToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("카드를 업데이트하기 위해서는 질문이 필요합니다.");
    }

    @Test
    @DisplayName("카드 수정 - 실패, 2000자를 넘긴 question")
    void updateCardWithLongQuestion() {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);

        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question(stringGenerator(2001))
                .answer("answer")
                .workbookId(workbookId)
                .encounterCount(0)
                .bookmark(true)
                .nextQuiz(true)
                .build();

        // when
        final HttpResponse response = 유저_카드_수정_요청(cardUpdateRequest, cardResponse, joanneToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("질문은 최대 2000자까지 입력 가능합니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "\r\n", "\r"})
    @DisplayName("카드 수정 - 실패, 유효하지 않은 answer")
    void updateCardWithInvalidAnswer(String answer) {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);

        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question("question")
                .answer(answer)
                .workbookId(workbookId)
                .encounterCount(0)
                .bookmark(true)
                .nextQuiz(true)
                .build();

        // when
        final HttpResponse response = 유저_카드_수정_요청(cardUpdateRequest, cardResponse, joanneToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("카드를 업데이트하기 위해서는 답변이 필요합니다.");
    }

    @Test
    @DisplayName("카드 수정 - 실패, 2000자를 넘긴 answer")
    void updateCardWithLongAnswer() {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);

        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question("question")
                .answer(stringGenerator(2001))
                .workbookId(workbookId)
                .encounterCount(0)
                .bookmark(true)
                .nextQuiz(true)
                .build();

        // when
        final HttpResponse response = 유저_카드_수정_요청(cardUpdateRequest, cardResponse, joanneToken);


        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("답변은 최대 2000자까지 입력 가능합니다.");
    }

    @Test
    @DisplayName("카드 수정 - 실패, 존재하지 않는 유저")
    void updateCardWithNotExistUser() {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);

        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(workbookId)
                .encounterCount(0)
                .bookmark(true)
                .nextQuiz(true)
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/{id}", cardUpdateRequest, cardResponse.getId())
                .failAuth() // 100L
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse).extracting("message").isEqualTo("해당 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("카드 수정 - 실패, 작성자가 아닌 유저")
    void updateCardWithNotAuthor() {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);

        CardUpdateRequest cardUpdateRequest = CardUpdateRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(workbookId)
                .encounterCount(0)
                .bookmark(true)
                .nextQuiz(true)
                .build();

        final String anotherToken = 소셜_로그인되어_있음(ditto, SocialType.GOOGLE);

        // when
        final HttpResponse response = 유저_카드_수정_요청(cardUpdateRequest, cardResponse, anotherToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse).extracting("message").isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @Test
    @DisplayName("카드 삭제 - 성공")
    void deleteCard() {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);

        // when
        final HttpResponse response = 유저_카드_삭제_요청(cardResponse, joanneToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("카드 삭제 - 실패, 존재하지 않는 유저")
    void deleteCardWithNotExistUser() {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);

        // when
        final HttpResponse response = request()
                .delete("/api/cards/" + cardResponse.getId())
                .failAuth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse).extracting("message").isEqualTo("해당 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("카드 삭제 - 실패, 작성자가 아닌 유저")
    void deleteCardWithNotAuthor() {
        // given
        final CardResponse cardResponse = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken);
        final String anotherToken = 소셜_로그인되어_있음(ditto, SocialType.GOOGLE);

        // when
        final HttpResponse response = 유저_카드_삭제_요청(cardResponse, anotherToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse).extracting("message").isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 성공")
    void selectNextQuizCards() {
        // given
        final Long card1 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();
        final Long card2 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();
        final Long card3 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();

        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(List.of(card1, card2, card3))
                .build();

        // when
        final HttpResponse response = 카드_또_보기_요청(request, joanneToken);
        ;

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
                .failAuth()
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).extracting("message").isEqualTo("유효하지 않은 또 보기 카드 등록 요청입니다.");
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 실패, 로그인하지 않은 경우")
    void selectNextQuizCardsWhenUserNotLogin() {
        // given
        final Long card1 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();
        final Long card2 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();
        final Long card3 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();

        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(List.of(card1, card2, card3))
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/next-quiz", request)
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(errorResponse).extracting("message").isEqualTo("토큰이 유효하지 않습니다.");
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 실패, 유저가 존재하지 않는 경우")
    void selectNextQuizCardsWhenUserNotFound() {
        // given
        final Long card1 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();
        final Long card2 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();
        final Long card3 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();

        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(List.of(card1, card2, card3))
                .build();

        // when
        final HttpResponse response = request()
                .put("/api/cards/next-quiz", request)
                .failAuth() // 100L
                .build();

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse).extracting("message").isEqualTo("해당 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 실패, author가 아닌 경우")
    void selectNextQuizCardsWhenUserIsNotAuthor() {
        // given
        final Long card1 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();
        final Long card2 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();
        final Long card3 = 유저_카드_등록되어_있음("question", "answer", workbookId, joanneToken).getId();

        final String anotherToken = 소셜_로그인되어_있음(kyle, SocialType.GOOGLE);

        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(List.of(card1, card2, card3))
                .build();

        // when
        final HttpResponse response = 카드_또_보기_요청(request, anotherToken);

        // then
        ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse).extracting("message").isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    private HttpResponse 유저_카드_수정_요청(CardUpdateRequest cardUpdateRequest,
                                     CardResponse cardResponse,
                                     String accessToken) {
        return request()
                .put("/api/cards/{id}", cardUpdateRequest, cardResponse.getId())
                .auth(accessToken)
                .build();
    }

    private HttpResponse 유저_카드_삭제_요청(CardResponse cardResponse, String accessToken) {
        return request()
                .delete("/api/cards/{id}", cardResponse.getId())
                .auth(accessToken)
                .build();
    }

    private HttpResponse 카드_또_보기_요청(NextQuizCardsRequest request, String accessToken) {
        return request()
                .put("/api/cards/next-quiz", request)
                .auth(accessToken)
                .build();
    }
}
