package botobo.core.category.application;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import botobo.core.category.dto.CategoryResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

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
            Category.builder().id(1L).build(),
            Category.builder().id(2L).build(),
            Category.builder().id(3L).build()
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