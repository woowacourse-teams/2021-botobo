package botobo.core.category.application;

import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import botobo.core.category.domain.CategoryRepository;
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
        given(categoryRepository.findAll()).willReturn(anyList());

        // when
        categoryService.findAll();

        // then
        then(categoryRepository)
                .should(times(1))
                .findAll();
    }
}