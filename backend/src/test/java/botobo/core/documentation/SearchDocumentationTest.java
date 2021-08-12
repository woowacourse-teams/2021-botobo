package botobo.core.documentation;

import botobo.core.application.SearchService;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.user.SimpleUserResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.search.SearchController;
import botobo.core.ui.search.SearchKeyword;
import botobo.core.ui.search.WorkbookSearchParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("검색 문서화 테스트")
@WebMvcTest(SearchController.class)
public class SearchDocumentationTest extends DocumentationTest {

    @MockBean
    private SearchService searchService;

    @Test
    @DisplayName("문제집 검색 - 성공")
    void searchWorkbooks() throws Exception {
        // given
        List<WorkbookResponse> workbookResponses = List.of(
                WorkbookResponse.builder()
                        .id(2L)
                        .name("피케이의 java 문제집")
                        .cardCount(15)
                        .author("pkeugine")
                        .tags(List.of(
                                TagResponse.builder().id(1L).name("java").build(),
                                TagResponse.builder().id(2L).name("backend").build()
                        ))
                        .build(),
                WorkbookResponse.builder()
                        .id(1L)
                        .name("중간곰의 java 문제집")
                        .cardCount(15)
                        .author("ggyool")
                        .tags(List.of(
                                TagResponse.builder().id(3L).name("자바").build(),
                                TagResponse.builder().id(4L).name("백엔드").build()
                        ))
                        .build()
        );
        given(searchService.searchWorkbooks(any(WorkbookSearchParameter.class))).willReturn(workbookResponses);

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/search/workbooks?type=name&criteria=date&order=desc&keyword=java&start=0&size=10")
                .build()
                .status(status().isOk())
                .identifier("search-workbooks-get-success");
    }

    @Test
    @DisplayName("태그 자동완성 - 성공")
    void searchTags() throws Exception {
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
        given(searchService.searchTags(any(SearchKeyword.class))).willReturn(tagResponses);

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/search/tags?keyword=Java")
                .build()
                .status(status().isOk())
                .identifier("search-tags-get-success");
    }

    @Test
    @DisplayName("태그 자동완성 - 성공")
    void searchUsers() throws Exception {
        // given
        List<SimpleUserResponse> userResponses = List.of(
                SimpleUserResponse.builder()
                        .id(1L)
                        .name("oz")
                        .build(),
                SimpleUserResponse.builder()
                        .id(2L)
                        .name("seozalue")
                        .build()
        );
        given(searchService.searchUsers(any(SearchKeyword.class))).willReturn(userResponses);

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/search/users?keyword=oz")
                .build()
                .status(status().isOk())
                .identifier("search-users-get-success");
    }
}
