package botobo.core.documentation;

import botobo.core.application.TagService;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.dto.tag.FilterCriteria;
import botobo.core.dto.tag.TagResponse;
import botobo.core.ui.TagController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("태그 문서화 테스트")
@WebMvcTest(TagController.class)
public class TagDocumentationTest extends DocumentationTest {

    @MockBean
    private TagService tagService;

    @DisplayName("키워드의 문제집명을 포함한 문제집의 태그를 가져온다. - 성공")
    @Test
    void findAllTagsByWorkbookName() throws Exception {
        // given
        List<TagResponse> tagResponses = List.of(
                TagResponse.builder()
                        .id(1L)
                        .name("java")
                        .build(),
                TagResponse.builder()
                        .id(2L)
                        .name("javascript")
                        .build()
        );

        given(tagService.findAllTagsByWorkbookName(any(FilterCriteria.class)))
                .willReturn(tagResponses);

        // when - then
        document()
                .mockMvc(mockMvc)
                .get("/tags?workbook=java")
                .build()
                .status(status().isOk())
                .identifier("tags-get-success");
    }
}
