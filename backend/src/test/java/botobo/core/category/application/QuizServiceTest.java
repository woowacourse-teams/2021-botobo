package botobo.core.category.application;

import botobo.core.card.domain.Answer;
import botobo.core.card.domain.Answers;
import botobo.core.card.domain.Card;
import botobo.core.card.domain.Cards;
import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import botobo.core.category.dto.QuizResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@DisplayName("퀴즈 서비스 테스트")
@ExtendWith(MockitoExtension.class)
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
                .logoUrl("")
                .description("")
                .isDeleted(false)
                .build();

        categoryWithOneCards = Category.builder()
                .name("name")
                .logoUrl("")
                .description("")
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
        given(categoryRepository.findCategoryAndCardsByIdJoinFetch(anyLong())).willReturn(Optional.of(category));

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(1);

        then(categoryRepository)
                .should(times(1))
                .findCategoryAndCardsByIdJoinFetch(anyLong());
    }

    @Test
    @DisplayName("카테고리 두 개의 id(Long)를 이용해서 2개의 카드가 담긴 퀴즈 생성 - 성공")
    void createQuizWithOneCards() {
        // given
        List<Long> ids = Arrays.asList(1L, 2L);
        given(categoryRepository.findCategoryAndCardsByIdJoinFetch(anyLong())).willReturn(Optional.of(categoryWithOneCards));

        // when
        List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(2);

        then(categoryRepository)
                .should(times(2))
                .findCategoryAndCardsByIdJoinFetch(anyLong());
    }
}