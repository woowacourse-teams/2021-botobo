package botobo.core.category.application;

import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import botobo.core.category.dto.CategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;

@MockitoSettings
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("목 카테고리 전체 조회 - 성공")
    void findAllWithMock() {
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
}