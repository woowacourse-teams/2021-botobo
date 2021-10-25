package botobo.core.domain.rank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SearchRanksTest {

    @Test
    @DisplayName("새로운 검색 랭크를 만든다 - 성공")
    void makeNewSearchRanks() {
        // given
        Set<String> newRankKeywords = new LinkedHashSet<>();
        newRankKeywords.add("react");
        newRankKeywords.add("java");
        newRankKeywords.add("redis");

        SearchRanks oldSearchRanks = new SearchRanks(Arrays.asList(
                new SearchRank("java", 1, 0),
                new SearchRank("react", 2, 0),
                new SearchRank("spring", 3, 0)
        ));

        // when
        List<SearchRank> newSearchRanks = oldSearchRanks.makeNewSearchRanks(newRankKeywords);

        // then
        assertThat(newSearchRanks).extracting(SearchRank::getKeyword)
                .containsExactly("react", "java", "redis");
        assertThat(newSearchRanks).extracting(SearchRank::getRank)
                .containsExactly(1, 2, 3);
        assertThat(newSearchRanks).extracting(SearchRank::getChange)
                .containsExactly(1, -1, null);
    }
}
