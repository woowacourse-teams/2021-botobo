package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.card.FixedCards;
import botobo.core.quiz.dto.QuizResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
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
    }

    @Test
    @DisplayName("문제집 id(Long)를 이용해서 1개의 카드가 담긴 퀴즈 생성 - 성공")
    void createQuiz() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbook));

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(1);

        then(workbookRepository)
                .should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("문제집 두 개의 id(Long)를 이용해서 2개의 카드가 담긴 퀴즈 생성 - 성공")
    void createQuizWithOneCards() {
        // given
        List<Long> ids = Arrays.asList(1L, 2L);
        given(workbookRepository.findById(anyLong())).willReturn(Optional.of(workbookWithOneCard));

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(2);

        then(workbookRepository)
                .should(times(2))
                .findById(anyLong());
    }

    @Test
    @DisplayName("첫 비회원용 퀴즈 생성 요청 - 성공")
    void createQuizForGuest() {
        FixedCards fixedCards = FixedCards.getInstance();
        if (!fixedCards.isFull()) {
            given(cardRepository.findFirst10By()).willReturn(cards);
        }
        // when
        List<QuizResponse> quizResponses = quizService.createQuizForGuest();

        //then
        assertThat(quizResponses.size()).isEqualTo(10);
    }
}
