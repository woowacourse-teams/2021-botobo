package botobo.core.acceptance.rank;

import botobo.core.acceptance.AcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.application.rank.SearchRankService;
import botobo.core.dto.rank.SearchRankResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RankAcceptanceTest extends AcceptanceTest {

    @Autowired
    private SearchRankService searchRankService;

    @Test
    @DisplayName("검색 키워드의 순위를 가져온다 - 성공")
    void findSearchKeywordRanks() {
        // given
        키워드로_여러번_검색_요청("java", 3);
        키워드로_여러번_검색_요청("react", 2);
        키워드로_여러번_검색_요청("spring", 1);
        searchRankService.updateSearchRanks(
                searchRankService.findSearchRanks(),
                searchRankService.findKeywordRanks()
        );

        // when
        final RequestBuilder.HttpResponse response = request()
                .get("/ranks/search")
                .build();

        // then
        List<SearchRankResponse> searchRankResponses = response.convertBodyToList(SearchRankResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(searchRankResponses).extracting(SearchRankResponse::getKeyword)
                .containsExactly("java", "react", "spring");
    }

    private void 키워드로_여러번_검색_요청(String keyword, int searchCount) {
        Map<String, String> parameters = new HashMap<>();
        for (int i = 0; i < searchCount; i++) {
            parameters.put("keyword", keyword);
            문제집_검색_요청(parameters);
        }
    }

    private RequestBuilder.HttpResponse 문제집_검색_요청(Map<String, String> parameters) {
        return request()
                .get("/search/workbooks")
                .queryParams(parameters)
                .build();
    }
}
