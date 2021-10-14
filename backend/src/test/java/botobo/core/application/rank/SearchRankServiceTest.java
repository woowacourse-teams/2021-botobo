package botobo.core.application.rank;

import botobo.core.dto.rank.SearchRankResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CacheManager cacheManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private SearchRankService searchRankService;

    @BeforeEach
    void setUp() {
        // 주석은 나중에 지우겠음
        // 캐시를 지우고 이전 검색 기록이 있는 상황 (일반적인 상황)
        // 으로 만들어 놓는다.
        getSearchRanksCache().clear();
        setScore("java", 10);
        setScore("spring", 5);
        setScore("react", 7);
        setScore("redis", 1);
        searchRankService.updateSearchRanks();
    }

    @AfterEach
    void tearDown() {
        redisConnectionFactory.getConnection().flushAll();
    }

    @Test
    @DisplayName("검색 순위를 가져온다 - 성공")
    void bringSearchRanks() {
        // given
        // 검색 이전에는 캐시가 비어있다.
        assertThat(isCacheEmpty()).isTrue();

        // when
        List<SearchRankResponse> searchRankResponses = searchRankService.bringSearchRanks();

        // then
        // 검색 후 캐시가 채워진다.
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
        // update를 하면 캐시가 지워진다는 것을 테스트하기 위해 캐시를 채워 놓는다.
        searchRankService.bringSearchRanks();
        assertThat(isCacheEmpty()).isFalse();

        setScore("react", 11);
        setScore("java", 5);
        setScore("redis", 7);
        setScore("spring", 1);

        // when
        searchRankService.updateSearchRanks();

        // then
        // update 후에는 캐시가 비워져 있다.
        assertThat(isCacheEmpty()).isTrue();

        List<SearchRankResponse> searchRankResponses = searchRankService.bringSearchRanks();
        assertThat(searchRankResponses).extracting(SearchRankResponse::getKeyword)
                .containsExactly("react", "redis", "java");
        assertThat(searchRankResponses).extracting(SearchRankResponse::getRank)
                .containsExactly(1, 2, 3);
        assertThat(searchRankResponses).extracting(SearchRankResponse::getChange)
                .containsExactly(1, null, -2);
    }

    private Cache getSearchRanksCache() {
        return cacheManager.getCache(SEARCH_RANKS_CACHE_VALUE);
    }

    private boolean isCacheEmpty() {
        // 이것보다 더 좋은 방법 없나 ?
        Map<Object, Object> map = (Map<Object, Object>) getSearchRanksCache().getNativeCache();
        return map.isEmpty();
    }

    private void setScore(String searchKeyword, int score) {
        for (int i = 0; i < score; i++) {
            searchRankService.increaseScore(searchKeyword);
        }
    }
}
