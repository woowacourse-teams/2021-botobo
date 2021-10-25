package botobo.core.domain.rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SearchRanks {

    private final List<SearchRank> searchRanks;

    public SearchRanks(List<SearchRank> searchRanks) {
        this.searchRanks = searchRanks;
    }

    public List<SearchRank> makeNewSearchRanks(Set<String> keywords) {
        List<SearchRank> newSearchRanks = new ArrayList<>();
        int newRank = 1;
        for (String keyword : keywords) {
            newSearchRanks.add(makeNewSearchRank(keyword, newRank++));
        }
        return newSearchRanks;
    }

    private SearchRank makeNewSearchRank(String keyword, int newRank) {
        Integer change = findRank(keyword)
                .map(rank -> rank - newRank)
                .orElse(null);
        return new SearchRank(keyword, newRank, change);
    }

    private Optional<Integer> findRank(String keyword) {
        return searchRanks.stream()
                .filter(searchRank -> searchRank.isSameKeyword(keyword))
                .findAny()
                .map(SearchRank::getRank);
    }
}
