package botobo.core.acceptance.card;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.User;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.card.NextQuizCardsRequest;
import botobo.core.dto.card.QuizRequest;
import botobo.core.dto.card.QuizResponse;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.exception.common.ErrorResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_CARD_REQUEST;
import static botobo.core.acceptance.utils.Fixture.MAKE_SINGLE_WORKBOOK_REQUEST;
import static botobo.core.acceptance.utils.Fixture.USER_JOANNE;
import static botobo.core.acceptance.utils.Fixture.USER_PK;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Quiz 인수 테스트")
public class QuizAcceptanceTest extends DomainAcceptanceTest {

    private List<Long> workbookIds;

    @Test
    @DisplayName("퀴즈 생성 - 성공")
    void createQuiz() {
        // given
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(workbookIds)
                .count(10)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("퀴즈 생성 - 실패, 비로그인은 회원용 퀴즈 생성을 이용할 수 없다.")
    void createQuizWhenGuest() {
        // given
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(workbookIds)
                .count(10)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .build();

        // when
        ErrorResponse errorResponse = response.convertToErrorResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(errorResponse.getMessage()).isEqualTo("토큰이 유효하지 않습니다.");
    }

    @Test
    @DisplayName("퀴즈 생성 - 실패, 회원을 찾을 수 없음.")
    void createQuizWhenUserNotFound() {
        // given
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(workbookIds)
                .count(10)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(100L))
                .build();

        // when
        ErrorResponse errorResponse = response.convertToErrorResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 유저를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("퀴즈 생성 - 실패, 내 문제집이 아님.")
    void createQuizWhenNotAuthor() {
        // given
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(workbookIds)
                .count(10)
                .build();

        // TODO USER_PK의 문제집임.
        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(USER_JOANNE.getId()))
                .build();

        // when
        ErrorResponse errorResponse = response.convertToErrorResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("작성자가 아니므로 권한이 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20, 25, 30})
    @DisplayName("퀴즈 생성 - 성공, 퀴즈 개수는 10 ~ 30사이의 수만 가능하다.")
    void createQuizWhenCountIsCorrect(int count) {
        // given
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(workbookIds)
                .count(count)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("퀴즈 생성 - 성공, 요청 개수가 가진 퀴즈 개수보다 많을 때, min 값을 취한다.")
    void createQuizWhenCountIsMoreThanWeHave() {
        // given
        final int requestCount = 11;
        final int existCardCount = 10;

        List<Long> ids = List.of(workbookIds.get(0));
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(ids)
                .count(requestCount)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(existCardCount);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 9, 31})
    @DisplayName("퀴즈 생성 - 실패, 퀴즈 개수는 10 ~ 30사이의 수만 가능하다.")
    void createQuizWhenQuizCountIsInvalid(int count) {
        // given
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(workbookIds)
                .count(count)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(1L))
                .build();

        // when
        ErrorResponse errorResponse = response.convertToErrorResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("퀴즈의 개수는 10 ~ 30 사이의 수만 가능합니다.");
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 퀴즈 생성 시 encounterCount가 1 증가한다. - 성공")
    void checkEncounterCount() {
        // given
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(workbookIds)
                .count(10)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(10);
        for (QuizResponse quizResponse : quizResponses) {
            assertThat(quizResponse.getEncounterCount()).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 퀴즈 생성 - 실패, 문제집 id가 비어있음")
    void createQuizWithEmptyWorkbookIdList() {
        // given
        List<Long> ids = Collections.emptyList();
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(ids)
                .count(10)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(1L))
                .build();

        // when, then
        final ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("퀴즈를 진행하려면 문제집 아이디가 필요합니다.");
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 퀴즈 생성 - 실패, 존재하지 않는 ID")
    void createQuizWithNotExistId() {
        // given
        List<Long> ids = Arrays.asList(1000L, 1001L, 1002L);
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(ids)
                .count(10)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(1L))
                .build();

        // when, then
        final ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 문제집을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 퀴즈 생성 - 실패, 퀴즈에 카드가 존재하지 않음.")
    void createQuizWithEmptyCards() {
        // given
        Long noCardWorkbookId = workbookIds.get(3);
        List<Long> ids = Collections.singletonList(noCardWorkbookId);
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(ids)
                .count(10)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // when, then
        final ErrorResponse errorResponse = response.convertToErrorResponse();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("퀴즈에 문제가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("다음에 또 보기가 포함된 카드를 퀴즈에 포함한다. - 성공")
    void createQuizIncludeNextQuizOption() {
        // given
        List<CardResponse> cards = COLLECT_CARDS_FROM_WORKBOOK(
                workbookIds.get(0),
                USER_PK
        ).getCards();

        final List<Long> cardIds = cards.stream()
                .map(CardResponse::getId)
                .collect(Collectors.toList());

        다음에_또_보기(cardIds);
        QuizRequest quizRequest = QuizRequest.builder()
                .workbookIds(workbookIds)
                .count(10)
                .build();

        final HttpResponse response = request()
                .post("/quizzes", quizRequest)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(10);
        assertThat(quizResponses)
                .extracting(QuizResponse::getId)
                .containsAll(cardIds);
    }

    public ExtractableResponse<Response> 다음에_또_보기(List<Long> cardIds) {
        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(cardIds)
                .build();

        return request()
                .put("/cards/next-quiz", request)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build()
                .extract();
    }

    @Test
    @DisplayName("비회원용 퀴즈 생성 - 성공")
    void createQuizForGuest() {
        final HttpResponse response = request()
                .get("/quizzes/guest")
                .build();

        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("비회원용 퀴즈 생성을 여러번 요청해도 동일한 퀴즈를 제공한다. - 성공")
    void createQuizForGuestMultipleRequest() {
        // given
        final HttpResponse firstResponse = request()
                .get("/quizzes/guest")
                .build();
        List<QuizResponse> firstQuizResponses = firstResponse.convertBodyToList(QuizResponse.class);

        final HttpResponse secondResponse = request()
                .get("/quizzes/guest")
                .build();
        List<QuizResponse> secondQuizResponses = firstResponse.convertBodyToList(QuizResponse.class);

        // when - then
        assertThat(firstResponse.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(firstQuizResponses.size()).isEqualTo(10);

        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(secondQuizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 성공")
    void createQuizFromWorkbook() {
        // given
        // 1번 문제집에는 5개의 카드가 존재한다.
        Long workbookId = workbookIds.get(0);
        final HttpResponse response = request()
                .get("/quizzes/" + workbookId)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(quizResponses).isNotEmpty();
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 존재하지 않는 문제집 아이디")
    void createQuizFromWorkbookWithNotExistId() {
        // given
        // 1번 문제집에는 5개의 카드가 존재한다.
        final HttpResponse response = request()
                .get("/quizzes/{workbookId}", 1000L)
                .auth(CREATE_TOKEN(USER_PK.getId()))
                .build();

        // when
        final ErrorResponse errorResponse = response.convertToErrorResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 문제집을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 문제집이 Public하지 않음")
    void createQuizFromWorkbookFailedWhenWorkbookIsNotPublic() {
        // given
        // 1번 문제집에는 5개의 카드가 존재한다.
        Long privateWorkbookId = workbookIds.get(4);
        final HttpResponse response = request()
                .get("/quizzes/{workbookId}", privateWorkbookId)
                .auth(CREATE_TOKEN(-100L))
                .build();

        // when
        final ErrorResponse errorResponse = response.convertToErrorResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("해당 문제집을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 퀴즈에 문제가 존재하지 않음")
    void createQuizFromWorkbookWithEmptyCards() {
        // given
        // 4번 문제집에는 카드가 존재하지 않는다.
        // 4번 문제집은 isPublic = true
        Long noCardWorkbookId = workbookIds.get(3);
        final HttpResponse response = request()
                .get("/quizzes/{workbookId}", noCardWorkbookId)
                .auth(CREATE_TOKEN(-100L))
                .build();

        // when
        final ErrorResponse errorResponse = response.convertToErrorResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("퀴즈에 문제가 존재하지 않습니다.");
    }

    @BeforeEach
    void setFixture() {
        List<WorkbookRequest> workbookRequests =
                List.of(
                        MAKE_SINGLE_WORKBOOK_REQUEST("1", true, emptyList()),
                        MAKE_SINGLE_WORKBOOK_REQUEST("2", true, emptyList()),
                        MAKE_SINGLE_WORKBOOK_REQUEST("3", true, emptyList()),
                        MAKE_SINGLE_WORKBOOK_REQUEST("4", true, emptyList()),
                        MAKE_SINGLE_WORKBOOK_REQUEST("5", false, emptyList())
                );
        workbookIds = CREATE_WORKBOOKS(workbookRequests, USER_PK);
        CREATE_CARDS(make30DummyQuestionAndAnswerSets(workbookIds), USER_PK);
    }

    private List<CardRequest> make30DummyQuestionAndAnswerSets(List<Long> workbookIds) {
        List<CardRequest> cardRequests = new ArrayList<>();
        List<String> dummySets = makeDummy30QuestionAndAnswerSets();
        for (int i = 0; i < dummySets.size(); i++) {
            String dummySet = dummySets.get(i);
            String[] questionAndAnswer = dummySet.split(" ");
            cardRequests.add(
                    MAKE_SINGLE_CARD_REQUEST(
                            questionAndAnswer[0],
                            questionAndAnswer[1],
                            workbookIds.get(i / 10)
                    )
            );
        }
        return cardRequests;
    }

    private List<String> makeDummy30QuestionAndAnswerSets() {
        List<String> results = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            results.add(i + " answer");
        }
        return results;
    }

    protected WorkbookCardResponse COLLECT_CARDS_FROM_WORKBOOK(Long workbookId, User user) {
        HttpResponse response = request()
                .get("/workbooks/{id}/cards", workbookId)
                .auth(CREATE_TOKEN(user.getId()))
                .build();
        return response.convertBody(WorkbookCardResponse.class);
    }
}
