package botobo.core.documentation;

import botobo.core.application.TagService;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.tag.WorkbookName;
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
        List<TagResponse> tagResponses = TagResponse.listOf(
                Tags.of(List.of(
                        Tag.of("java"),
                        Tag.of("spring"),
                        Tag.of("자바")
                ))
        );
        given(tagService.findAllTagsByWorkbookName(any(WorkbookName.class)))
                .willReturn(tagResponses);

        // when - then
        document()
                .mockMvc(mockMvc)
                .get("/api/tags?workbook=java")
                .build()
                .status(status().isOk())
                .identifier("tags-get-success");
    }
}
