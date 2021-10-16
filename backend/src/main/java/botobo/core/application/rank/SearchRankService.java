package botobo.core.application.rank;

import botobo.core.domain.rank.SearchRank;
import botobo.core.domain.rank.SearchRankRepository;
import botobo.core.domain.rank.SearchRanks;
import botobo.core.domain.rank.SearchScoreRepository;
import botobo.core.dto.rank.SearchRankResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class SearchRankService {

    private static final String SEARCH_RANKS_CACHE_VALUE = "SearchRanks";
    private static final int SEARCH_RANK_COUNT = 3;

    private final SearchRankRepository searchRankRepository;
    private final SearchScoreRepository searchScoreRepository;

    public SearchRankService(SearchRankRepository searchRankRepository, SearchScoreRepository searchScoreRepository) {
        this.searchRankRepository = searchRankRepository;
        this.searchScoreRepository = searchScoreRepository;
    }

    @Cacheable(value = SEARCH_RANKS_CACHE_VALUE, key = "'SearchRanksKey'")
    public List<SearchRankResponse> bringSearchRanks() {
        List<SearchRank> searchRanks = findSearchRanks();
        return SearchRankResponse.listOf(searchRanks);
    }

    public List<SearchRank> findSearchRanks() {
        return searchRankRepository.findAll();
    }

    public Set<String> findKeywordRanks() {
        return searchScoreRepository.findByScoreDesc(SEARCH_RANK_COUNT);
    }

    // 해당 메서드에 인자가 생기면서 Key 값을 정해줘야 했는데, SPEL 문법을 따라야 해서 아래처럼 만들었음
    // 참고: https://stackoverflow.com/questions/33383366/cacheble-annotation-on-no-parameter-method

    // 이 메서드에서 예외가 발생하면 deleteAll, pushAll은 레디스에 당연히 반영 안 되고, Cache도 지워지지 않는 것을 확인하였음
    @Transactional
    @CacheEvict(value = SEARCH_RANKS_CACHE_VALUE, key = "'SearchRanksKey'")
    public void updateSearchRanks(List<SearchRank> oldSearchRankList, Set<String> newKeywordRanks) {
        SearchRanks oldSearchRanks = new SearchRanks(oldSearchRankList);
        searchRankRepository.deleteAll();
        searchRankRepository.pushAll(oldSearchRanks.makeNewSearchRanks(newKeywordRanks));
        searchScoreRepository.deleteAll();
    }

    @Transactional
    public void increaseScore(String searchKeyword) {
        searchScoreRepository.increaseScore(searchKeyword);
    }
}
