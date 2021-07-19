package botobo.core.quiz.application;

import botobo.core.quiz.domain.answer.Answer;
import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.category.Category;
import botobo.core.quiz.domain.category.CategoryRepository;
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
    private CategoryRepository categoryRepository;

    @InjectMocks
    private QuizService quizService;

    private Category category;
    private Category categoryWithOneCards;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("name")
                .isDeleted(false)
                .build();

        categoryWithOneCards = Category.builder()
                .name("name")
                .isDeleted(false)
                .build();

        Card card1 = Card.builder()
                .id(1L)
                .question("question")
                .category(category)
                .build();

        Card card2 = Card.builder()
                .id(2L)
                .question("question")
                .category(categoryWithOneCards)
                .build();

        Answer answer1 = Answer.builder()
                .content("content")
                .card(card1)
                .build();

        Answer answer2 = Answer.builder()
                .content("content")
                .card(card2)
                .build();
    }

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 1개의 카드가 담긴 퀴즈 생성 - 성공")
    void createQuiz() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(1);

        then(categoryRepository)
                .should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("카테고리 두 개의 id(Long)를 이용해서 2개의 카드가 담긴 퀴즈 생성 - 성공")
    void createQuizWithOneCards() {
        // given
        List<Long> ids = Arrays.asList(1L, 2L);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(categoryWithOneCards));

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(2);

        then(categoryRepository)
                .should(times(2))
                .findById(anyLong());
    }
}