package botobo.core.acceptance.card;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.card.NextQuizCardsRequest;
import botobo.core.dto.card.QuizRequest;
import botobo.core.dto.card.QuizResponse;
import botobo.core.exception.ErrorResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static botobo.core.utils.Fixture.CARD_REQUEST_1;
import static botobo.core.utils.Fixture.CARD_REQUEST_10;
import static botobo.core.utils.Fixture.CARD_REQUEST_11;
import static botobo.core.utils.Fixture.CARD_REQUEST_12;
import static botobo.core.utils.Fixture.CARD_REQUEST_13;
import static botobo.core.utils.Fixture.CARD_REQUEST_14;
import static botobo.core.utils.Fixture.CARD_REQUEST_15;
import static botobo.core.utils.Fixture.CARD_REQUEST_2;
import static botobo.core.utils.Fixture.CARD_REQUEST_3;
import static botobo.core.utils.Fixture.CARD_REQUEST_4;
import static botobo.core.utils.Fixture.CARD_REQUEST_5;
import static botobo.core.utils.Fixture.CARD_REQUEST_6;
import static botobo.core.utils.Fixture.CARD_REQUEST_7;
import static botobo.core.utils.Fixture.CARD_REQUEST_8;
import static botobo.core.utils.Fixture.CARD_REQUEST_9;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_1;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_2;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_3;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_4;
import static botobo.core.utils.Fixture.WORKBOOK_REQUEST_5;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Quiz 인수 테스트")
public class QuizAcceptanceTest extends DomainAcceptanceTest {

    @BeforeEach
    void setFixture() {
        여러개_문제집_생성_요청(Arrays.asList(WORKBOOK_REQUEST_1, WORKBOOK_REQUEST_2, WORKBOOK_REQUEST_3,
                WORKBOOK_REQUEST_4, WORKBOOK_REQUEST_5));
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

        final HttpResponse response = request()
                .post("/api/quizzes", quizRequest)
                .failAuth()
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 퀴즈 생성 시 encounterCount가 1 증가한다. - 성공")
    void checkEncounterCount() {
        // given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        QuizRequest quizRequest =
                new QuizRequest(ids);

        final HttpResponse response = request()
                .post("/api/quizzes", quizRequest)
                .failAuth()
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
        QuizRequest quizRequest =
                new QuizRequest(ids);

        final HttpResponse response = request()
                .post("/api/quizzes", quizRequest)
                .failAuth()
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
        QuizRequest quizRequest =
                new QuizRequest(ids);

        final HttpResponse response = request()
                .post("/api/quizzes", quizRequest)
                .failAuth()
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
        List<Long> ids = Collections.singletonList(4L);
        QuizRequest quizRequest = new QuizRequest(ids);

        final HttpResponse response = request()
                .post("/api/quizzes", quizRequest)
                .failAuth()
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
        다음에_또_보기(List.of(1L, 2L, 3L));
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        QuizRequest quizRequest =
                new QuizRequest(ids);

        final HttpResponse response = request()
                .post("/api/quizzes", quizRequest)
                .failAuth()
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(10);
        assertThat(quizResponses.stream()
                .map(QuizResponse::getId)
                .collect(Collectors.toList()))
                .containsAll(List.of(1L, 2L, 3L));
    }

    public ExtractableResponse<Response> 다음에_또_보기(List<Long> cardIds) {
        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(cardIds)
                .build();

        return request()
                .put("/api/cards/next-quiz", request)
                .auth(createToken(user.getId()))
                .build()
                .extract();
    }

    @Test
    @DisplayName("비회원용 퀴즈 생성 - 성공")
    void createQuizForGuest() {
        final HttpResponse response = request()
                .get("/api/quizzes/guest")
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
                .get("/api/quizzes/guest")
                .build();
        List<QuizResponse> firstQuizResponses = firstResponse.convertBodyToList(QuizResponse.class);

        final HttpResponse secondResponse = request()
                .get("/api/quizzes/guest")
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
        final HttpResponse response = request()
                .get("/api/quizzes/{workbookId}", 1L)
                .failAuth()
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(quizResponses).isNotEmpty();
        assertThat(quizResponses.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 존재하지 않는 문제집 아이디")
    void createQuizFromWorkbookWithNotExistId() {
        // given
        // 1번 문제집에는 5개의 카드가 존재한다.
        final HttpResponse response = request()
                .get("/api/quizzes/{workbookId}", 100L)
                .failAuth()
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
        final HttpResponse response = request()
                .get("/api/quizzes/{workbookId}", 5L)
                .failAuth()
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
        final HttpResponse response = request()
                .get("/api/quizzes/{workbookId}", 4L)
                .failAuth()
                .build();

        // when
        final ErrorResponse errorResponse = response.convertToErrorResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("퀴즈에 문제가 존재하지 않습니다.");
    }
}