package botobo.core.quiz;

import botobo.core.DomainAcceptanceTest;
import botobo.core.common.exception.ErrorResponse;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import botobo.core.quiz.dto.QuizRequest;
import botobo.core.quiz.dto.QuizResponse;
import botobo.core.utils.RequestBuilder.HttpResponse;
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
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Quiz 인수 테스트")
public class QuizAcceptanceTest extends DomainAcceptanceTest {

    @BeforeEach
    void setFixture() {
        여러개_문제집_생성_요청(Arrays.asList(WORKBOOK_REQUEST_1, WORKBOOK_REQUEST_2, WORKBOOK_REQUEST_3, WORKBOOK_REQUEST_4));
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
                .auth()
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 퀴즈 생성 - 실패, 문제집 id가 비어있음")
    void createQuizWithEmptyCategoryIdList() {
        // given
        List<Long> ids = Collections.emptyList();
        QuizRequest quizRequest =
                new QuizRequest(ids);

        final HttpResponse response = request()
                .post("/api/quizzes", quizRequest)
                .auth()
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
                .auth()
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
                .auth()
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
                .auth()
                .build();

        // when
        final List<QuizResponse> quizResponses = response.convertBodyToList(QuizResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quizResponses.size()).isEqualTo(10);
        assertThat(quizResponses.stream()
                .map(QuizResponse::getQuestion)
                .collect(Collectors.toList()))
                .containsAll(List.of("1", "2", "3"));
    }

    public ExtractableResponse<Response> 다음에_또_보기(List<Long> cardIds) {
        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(cardIds)
                .build();

        return request()
                .put("/api/cards/next-quiz", request)
                .auth()
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
}
