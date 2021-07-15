package botobo.core.quiz.application;

import botobo.core.quiz.domain.answer.Answer;
import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.category.Category;
import botobo.core.quiz.domain.category.CategoryRepository;
import botobo.core.quiz.dto.CategoryCardsResponse;
import botobo.core.quiz.dto.CategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@DisplayName("카테고리 서비스 테스트")
@MockitoSettings
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("더미 카테고리 전체 조회 - 성공")
    void findAllWithDummy() {
        // given
        List<Category> categories = Arrays.asList(
                Category.builder().id(1L).name("a").logoUrl("").isDeleted(false).description("").build(),
                Category.builder().id(2L).name("b").logoUrl("").isDeleted(false).description("").build(),
                Category.builder().id(3L).name("c").logoUrl("").isDeleted(false).description("").build()
        );
        given(categoryRepository.findAll()).willReturn(categories);

        // when
        List<CategoryResponse> categoryResponses = categoryService.findAll();

        // then
        then(categoryRepository)
                .should(times(1))
                .findAll();
    }

    @Test
    @DisplayName("더미 카테고리 카드 모아보기 - 성공")
    void findCategoryCardsWithDummy() {
        // given
        Category category = Category.builder()
                .id(1L)
                .name("java")
                .isDeleted(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();
        Card card = Card.builder()
                .id(1L)
                .question("질문")
                .category(category)
                .build();
        Answer answer = Answer.builder()
                .id(1L)
                .content("내용입니다.")
                .card(card)
                .build();

        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        CategoryCardsResponse categoryCardsResponse = categoryService.findCategoryCardsById(category.getId());

        // then
        assertThat(categoryCardsResponse.getCategoryName()).isEqualTo("java");
        assertThat(categoryCardsResponse.getCards().size()).isEqualTo(1);

        then(categoryRepository)
                .should(times(1))
                .findById(anyLong());
    }
}