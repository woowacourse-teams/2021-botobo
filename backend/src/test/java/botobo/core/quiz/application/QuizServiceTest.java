package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.QuizResponse;
import botobo.core.quiz.exception.QuizEmptyException;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

@DisplayName("퀴즈 서비스 테스트")
@MockitoSettings
class QuizServiceTest {

    @Mock
    private WorkbookRepository workbookRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private QuizService quizService;

    private Workbook workbook;
    private Workbook workbookWithOneCard;
    private List<Card> cards;
    private List<Card> twoCards;

    @BeforeEach
    void setUp() {
        workbook = Workbook.builder()
                .name("name")
                .isDeleted(false)
                .build();

        workbookWithOneCard = Workbook.builder()
                .name("name")
                .isDeleted(false)
                .build();

        Card card1 = Card.builder()
                .id(1L)
                .question("question")
                .answer("answer")
                .workbook(workbook)
                .build();

        Card card2 = Card.builder()
                .id(2L)
                .question("question")
                .answer("answer")
                .workbook(workbookWithOneCard)
                .build();

        cards = Arrays.asList(card1, card1, card1, card1, card1, card1, card1, card1, card1, card1);
        twoCards = Arrays.asList(card1, card2);
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 10개의 카드가 담긴 퀴즈 생성 - 성공")
    void createQuiz() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        given(workbookRepository.existsById(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookIds(any())).willReturn(cards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(10);

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookIds(any());
    }

    @Test
    @DisplayName("문제집 id를 이용해 퀴즈 생성 시 마주친 횟수가 증가한다. - 성공")
    void createQuizIncrementEncounterCount() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        given(workbookRepository.existsById(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookIds(any())).willReturn(twoCards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(2);
        for (QuizResponse quizResponse : quizResponses) {
            assertThat(quizResponse.getEncounterCount()).isEqualTo(1);
        }

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookIds(any());
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 10개의 카드가 담긴 퀴즈 생성 - 실패, 문제집 아이디가 존재하지 않음")
    void createQuizFailedWhenIdNotFound() {
        // given
        List<Long> ids = Collections.singletonList(100L);
        given(workbookRepository.existsById(any())).willReturn(false);

        // when
        assertThatThrownBy(() -> quizService.createQuiz(ids))
                .isInstanceOf(WorkbookNotFoundException.class);

        then(cardRepository)
                .should(times(0))
                .findCardsByWorkbookIds(any());
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 10개의 카드가 담긴 퀴즈 생성 - 실패, 퀴즈에 문제가 존재하지 않음")
    void createQuizFailedWhenQuizIsEmpty() {
        // given
        List<Long> ids = Collections.singletonList(100L);
        List<Card> emptyCards = new ArrayList<>();
        given(workbookRepository.existsById(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookIds(ids)).willReturn(emptyCards);

        // when
        assertThatThrownBy(() -> quizService.createQuiz(ids))
                .isInstanceOf(QuizEmptyException.class);

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookIds(any());
    }

    @Test
    @DisplayName("문제집 id(Long) 및 다음에 또보기 카드 10개를 포함한 퀴즈 생성 - 성공")
    void createQuizWithOneCards() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        given(workbookRepository.existsById(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookIds(any())).willReturn(cards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(10);

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookIds(any());
    }

    @Test
    @DisplayName("첫 비회원용 퀴즈 생성 요청 - 성공")
    void createQuizForGuest() {
        // when
        List<QuizResponse> quizResponses = quizService.createQuizForGuest();

        //then
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 성공")
    void createQuizFromWorkbook() {
        // given
        Long workbookId = 1L;
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookId(workbookId)).willReturn(cards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuizFromWorkbook(workbookId);

        // then
        assertThat(quizResponses.size()).isEqualTo(10);

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookId(any());
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 시 마주친 횟수는 증가하지 않는다. - 성공")
    void createQuizFromWorkbookNotIncrementEncounterCount() {
        // given
        Long workbookId = 1L;
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookId(workbookId)).willReturn(cards);

        // when
        List<QuizResponse> quizResponses = quizService.createQuizFromWorkbook(workbookId);

        // then
        assertThat(quizResponses.size()).isEqualTo(10);
        for (QuizResponse quizResponse : quizResponses) {
            assertThat(quizResponse.getEncounterCount()).isEqualTo(0);
        }

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookId(any());
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 문제집 아이디가 존재하지 않음.")
    void createQuizFromWorkbookFailed() {
        // given
        Long workbookId = 100L;
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(false);

        // when
        assertThatThrownBy(() -> quizService.createQuizFromWorkbook(workbookId))
                .isInstanceOf(WorkbookNotFoundException.class);

        then(cardRepository)
                .should(times(0))
                .findCardsByWorkbookId(any());
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 문제집이 Public 아님.")
    void createQuizFromWorkbookFailedWhenWorkbookIsNotPublic() {
        // given
        Long workbookId = 1L;
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(false);

        // when
        assertThatThrownBy(() -> quizService.createQuizFromWorkbook(workbookId))
                .isInstanceOf(WorkbookNotFoundException.class);

        then(cardRepository)
                .should(times(0))
                .findCardsByWorkbookId(any());
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 퀴즈에 문제가 존재하지 않음.")
    void createQuizFromWorkbookFailedWhenQuizIsEmpty() {
        // given
        Long workbookId = 1L;
        List<Card> emptyCards = new ArrayList<>();
        given(workbookRepository.existsByIdAndOpenedTrue(any())).willReturn(true);
        given(cardRepository.findCardsByWorkbookId(workbookId)).willReturn(emptyCards);

        // when
        assertThatThrownBy(() -> quizService.createQuizFromWorkbook(workbookId))
                .isInstanceOf(QuizEmptyException.class);

        then(cardRepository)
                .should(times(1))
                .findCardsByWorkbookId(any());
    }
}
