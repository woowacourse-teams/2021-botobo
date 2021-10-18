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

    @Cacheable(value = SEARCH_RANKS_CACHE_VALUE, key = "'SearchRanksKey'", cacheManager = "concurrentMapCacheManager")
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

    @Transactional
    @CacheEvict(value = SEARCH_RANKS_CACHE_VALUE, key = "'SearchRanksKey'", cacheManager = "concurrentMapCacheManager")
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
