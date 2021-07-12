package botobo.core.documentation;

import botobo.core.quiz.application.CategoryService;
import botobo.core.quiz.dto.CategoryResponse;
import botobo.core.quiz.ui.CategoryController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static botobo.core.documentation.DocumentationUtils.getDocumentRequest;
import static botobo.core.documentation.DocumentationUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("카테고리 문서화 테스트")
@WebMvcTest(CategoryController.class)
@AutoConfigureRestDocs
public class CategoryDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("카레고리 전체 조회 - 성공")
    void findAllCategories() throws Exception {
        // given
        given(categoryService.findAll()).willReturn(generateCategoryResponse());

        // when, then
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andDo(document("categories-get-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    private List<CategoryResponse> generateCategoryResponse() {
        return Arrays.asList(
                CategoryResponse.builder()
                        .id(1L)
                        .name("Java")
                        .description("스프링의 기초가 되는 언어")
                        .cardCount(20)
                        .logoUrl("botobo.io")
                        .build(),
                CategoryResponse.builder()
                        .id(2L)
                        .name("Javascript")
                        .description("프론트엔드의 기초가 되는 언어")
                        .cardCount(15)
                        .logoUrl("botobo.io")
                        .build(),
                CategoryResponse.builder()
                        .id(3L)
                        .name("React")
                        .description("프론트엔드의 요즘 기술")
                        .cardCount(8)
                        .logoUrl("botobo.io")
                        .build()
        );
    }
}
