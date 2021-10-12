package botobo.core.domain.rank;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SearchRank implements Serializable {

    private final String keyword;
    private final Integer rank;
    private final Integer change;

    public SearchRank(String keyword, Integer rank) {
        this(keyword, rank, null);
    }

    public SearchRank(String keyword, Integer rank, Integer change) {
        this.keyword = keyword;
        this.rank = rank;
        this.change = change;
    }

    public boolean isSameKeyword(String keyword) {
        return this.keyword.equals(keyword);
    }
}
