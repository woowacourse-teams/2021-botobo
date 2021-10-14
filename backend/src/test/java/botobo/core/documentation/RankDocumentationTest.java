package botobo.core.documentation;

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
public class RankDocumentationTest extends DocumentationTest {

    @MockBean
    private SearchRankService searchRankService;

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
}
