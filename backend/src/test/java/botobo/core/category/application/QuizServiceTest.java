package botobo.core.category.application;

import botobo.core.card.domain.Answer;
import botobo.core.card.domain.Card;
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
import static org.mockito.BDDMockito.given;

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
        Answer answer = Answer.builder()
                .content("content")
                .build();

        Card card = Card.builder()
                .id(1L)
                .question("question")
                .category(Category.builder().name("name").build())
                .answers(Arrays.asList(answer))
                .build();

        List<Card> cards = Arrays.asList(card, card, card, card, card, card, card, card, card, card, card);

        category = Category.builder()
                .cards(cards)
                .name("name")
                .build();

        categoryWithOneCards = Category.builder()
                .cards(Arrays.asList(card))
                .name("name")
                .build();
    }

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 성공")
    void createQuiz() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        // when
        final List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 1개의 카드가 담긴 퀴즈 생성 - 성공")
    void createQuizWithOneCards() {
        // given
        List<Long> ids = Collections.singletonList(1L);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(categoryWithOneCards));

        // when
        final List<QuizResponse> quizResponses = quizService.createQuiz(ids);

        // then
        assertThat(quizResponses.size()).isEqualTo(1);
    }
}