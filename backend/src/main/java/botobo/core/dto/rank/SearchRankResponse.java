package botobo.core.dto.rank;

import botobo.core.domain.rank.SearchRank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SearchRankResponse implements Serializable {

    private String keyword;
    private Integer rank;
    private Integer change;

    public static SearchRankResponse of(SearchRank searchRank) {
        return new SearchRankResponse(
                searchRank.getKeyword(),
                searchRank.getRank(),
                searchRank.getChange()
        );
    }

    public static List<SearchRankResponse> listOf(List<SearchRank> searchRanks) {
        return searchRanks.stream()
                .map(SearchRankResponse::of)
                .collect(Collectors.toList());
    }
}
