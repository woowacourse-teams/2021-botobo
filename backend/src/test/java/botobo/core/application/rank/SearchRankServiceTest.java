package botobo.core.application.rank;

import botobo.core.dto.rank.SearchRankResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class SearchRankServiceTest {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private SearchRankService searchRankService;

    @BeforeEach
    void setUp() {
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
    @DisplayName("검색 랭크를 가져온다 - 성공")
    void bringSearchRanks() {
        // when
        List<SearchRankResponse> searchRankResponses = searchRankService.bringSearchRanks();

        // then
        assertThat(searchRankResponses).extracting(SearchRankResponse::getKeyword)
                .containsExactly("java", "react", "spring");
        assertThat(searchRankResponses).extracting(SearchRankResponse::getRank)
                .containsExactly(1, 2, 3);
        assertThat(searchRankResponses).extracting(SearchRankResponse::getChange)
                .containsExactly(null, null, null);
    }

    @Test
    @DisplayName("검색 랭크를 업데이트한다 - 성공")
    void updateSearchRanks() {
        // given
        setScore("react", 11);
        setScore("java", 5);
        setScore("redis", 7);
        setScore("spring", 1);

        // when
        searchRankService.updateSearchRanks();

        // then
        List<SearchRankResponse> searchRankResponses = searchRankService.bringSearchRanks();
        assertThat(searchRankResponses).extracting(SearchRankResponse::getKeyword)
                .containsExactly("react", "redis", "java");
        assertThat(searchRankResponses).extracting(SearchRankResponse::getRank)
                .containsExactly(1, 2, 3);
        assertThat(searchRankResponses).extracting(SearchRankResponse::getChange)
                .containsExactly(1, null, -2);
    }

    private void setScore(String searchKeyword, int score) {
        for (int i = 0; i < score; i++) {
            searchRankService.increaseScore(searchKeyword);
        }
    }
}
