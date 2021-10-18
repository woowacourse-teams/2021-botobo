package botobo.core.documentation;

import botobo.core.application.WorkbookRankService;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.application.rank.SearchRankService;
import botobo.core.domain.rank.SearchRank;
import botobo.core.dto.rank.SearchRankResponse;
import botobo.core.ui.RankController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("랭크 문서화 테스트")
@WebMvcTest(RankController.class)
class RankDocumentationTest extends DocumentationTest {

    @MockBean
    private WorkbookRankService workbookRankService;

    @MockBean
    private SearchRankService searchRankService;

    @Test
    @DisplayName("문제집의 순위를 가져온다 - 성공")
    void findWorkbookRanks() throws Exception {
        // given
        given(workbookRankService.findWorkbookRanks()).willReturn(createWorkbookReponses());

        // when // then
        document()
                .mockMvc(mockMvc)
                .get("/ranks/workbooks")
                .build()
                .status(status().isOk())
                .identifier("ranks-workbooks-get-success");
    }

    @Test
    @DisplayName("검색 키워드의 순위를 가져온다 - 성공")
    void findSearchKeywordRanks() throws Exception {
        List<SearchRankResponse> searchRankResponses = Arrays.asList(
                SearchRankResponse.of(new SearchRank("java", 1, 1)),
                SearchRankResponse.of(new SearchRank("react", 2, -1)),
                SearchRankResponse.of(new SearchRank("spring", 3, null))
        );

        given(searchRankService.bringSearchRanks()).willReturn(searchRankResponses);

        document()
                .mockMvc(mockMvc)
                .get("/ranks/search")
                .build()
                .status(status().isOk())
                .identifier("ranks-search-get-success");
    }

    private List<WorkbookResponse> createWorkbookReponses() {
        return List.of(
                WorkbookResponse.builder()
                        .id(3L)
                        .name("카일의 react 문제집")
                        .cardCount(15)
                        .heartCount(20)
                        .author("GwangYeol-Im")
                        .tags(List.of(
                                TagResponse.builder().id(1L).name("javascript").build(),
                                TagResponse.builder().id(2L).name("frontend").build()
                        ))
                        .build(),
                WorkbookResponse.builder()
                        .id(2L)
                        .name("피케이의 java 문제집")
                        .cardCount(15)
                        .heartCount(10)
                        .author("pkeugine")
                        .tags(List.of(
                                TagResponse.builder().id(3L).name("java").build(),
                                TagResponse.builder().id(4L).name("backend").build()
                        ))
                        .build(),
                WorkbookResponse.builder()
                        .id(1L)
                        .name("조앤의 kubernates 문제집")
                        .cardCount(15)
                        .heartCount(8)
                        .author("seovalue")
                        .tags(List.of(
                                TagResponse.builder().id(5L).name("docker").build(),
                                TagResponse.builder().id(6L).name("infrastructure").build()
                        ))
                        .build()
        );
    }
}
