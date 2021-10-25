package botobo.core.application.rank;

import botobo.core.dto.rank.SearchRankResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class SearchRankServiceTest {

    private static final String SEARCH_RANKS_CACHE_VALUE = "SearchRanks";

    @Autowired
    @Qualifier("concurrentMapCacheManager")
    private CacheManager concurrentMapCacheManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private SearchRankService searchRankService;

    @BeforeEach
    void setUp() {
        getSearchRanksCache().clear();
        setScore("java", 10);
        setScore("spring", 5);
        setScore("react", 7);
        setScore("redis", 1);
        searchRankService.updateSearchRanks(
                searchRankService.findSearchRanks(),
                searchRankService.findKeywordRanks()
        );
    }

    @AfterEach
    void tearDown() {
        redisConnectionFactory.getConnection().flushAll();
    }

    @Test
    @DisplayName("검색 순위를 가져온다 - 성공")
    void bringSearchRanks() {
        // given
        assertThat(isCacheEmpty()).isTrue();

        // when
        List<SearchRankResponse> searchRankResponses = searchRankService.bringSearchRanks();

        // then
        assertThat(isCacheEmpty()).isFalse();

        assertThat(searchRankResponses).extracting(SearchRankResponse::getKeyword)
                .containsExactly("java", "react", "spring");
        assertThat(searchRankResponses).extracting(SearchRankResponse::getRank)
                .containsExactly(1, 2, 3);
        assertThat(searchRankResponses).extracting(SearchRankResponse::getChange)
                .containsExactly(null, null, null);
    }

    @Test
    @DisplayName("검색 순위를 업데이트한다 - 성공")
    void updateSearchRanks() {
        // given
        setScore("react", 11);
        setScore("java", 5);
        setScore("redis", 7);
        setScore("spring", 1);

        // when
        searchRankService.updateSearchRanks(
                searchRankService.findSearchRanks(),
                searchRankService.findKeywordRanks()
        );

        // then
        getSearchRanksCache().clear();
        List<SearchRankResponse> searchRankResponses = searchRankService.bringSearchRanks();
        assertThat(searchRankResponses).extracting(SearchRankResponse::getKeyword)
                .containsExactly("react", "redis", "java");
        assertThat(searchRankResponses).extracting(SearchRankResponse::getRank)
                .containsExactly(1, 2, 3);
        assertThat(searchRankResponses).extracting(SearchRankResponse::getChange)
                .containsExactly(1, null, -2);
    }

    @Test
    @DisplayName("검색 순위 캐시를 비운다 - 성공")
    void removeSearchRanksCache() {
        // given
        bringSearchRanks();
        assertThat(isCacheEmpty()).isFalse();

        // when
        searchRankService.removeSearchRanksCache();

        // then
        assertThat(isCacheEmpty()).isTrue();
    }

    private Cache getSearchRanksCache() {
        return concurrentMapCacheManager.getCache(SEARCH_RANKS_CACHE_VALUE);
    }

    private boolean isCacheEmpty() {
        Map<Object, Object> map = (Map<Object, Object>) getSearchRanksCache().getNativeCache();
        return map.isEmpty();
    }

    private void setScore(String searchKeyword, int score) {
        for (int i = 0; i < score; i++) {
            searchRankService.increaseScore(searchKeyword);
        }
    }
}
