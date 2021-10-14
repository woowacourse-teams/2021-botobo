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
@Transactional(readOnly = true)
public class SearchRankService {

    private static final String SEARCH_RANKS_CACHE_VALUE = "SearchRanks";
    private static final int SEARCH_RANK_COUNT = 3;

    private final SearchRankRepository searchRankRepository;
    private final SearchScoreRepository searchScoreRepository;

    public SearchRankService(SearchRankRepository searchRankRepository, SearchScoreRepository searchScoreRepository) {
        this.searchRankRepository = searchRankRepository;
        this.searchScoreRepository = searchScoreRepository;
    }

    @Transactional
    @Cacheable(SEARCH_RANKS_CACHE_VALUE)
    public List<SearchRankResponse> bringSearchRanks() {
        List<SearchRank> searchRanks = searchRankRepository.findAll();
        return SearchRankResponse.listOf(searchRanks);
    }

    @Transactional
    @CacheEvict(SEARCH_RANKS_CACHE_VALUE)
    public void updateSearchRanks() {
        SearchRanks oldSearchRanks = new SearchRanks(searchRankRepository.findAll());
        Set<String> keywords = searchScoreRepository.findByScoreDesc(SEARCH_RANK_COUNT);
        searchRankRepository.deleteAll();
        searchRankRepository.pushAll(oldSearchRanks.makeNewSearchRanks(keywords));
        searchScoreRepository.deleteAll();
    }

    @Transactional
    public void increaseScore(String searchKeyword) {
        searchScoreRepository.increaseScore(searchKeyword);
    }
}
