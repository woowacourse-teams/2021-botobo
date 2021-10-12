package botobo.core.domain.rank;

import botobo.core.config.redis.EmbeddedRedisConfig;
import botobo.core.config.redis.RedisConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@MockBean(JpaMetamodelMappingContext.class)
@ActiveProfiles("test")
@Import({EmbeddedRedisConfig.class, RedisConfig.class, SearchRankRepository.class})
class SearchRankRepositoryTest {

    @Autowired
    private SearchRankRepository searchRankRepository;

    @AfterEach
    void tearDown() {
        searchRankRepository.deleteAll();
    }

    @Test
    @DisplayName("Collection<SearchRank> 의 데이터를 푸시한다 - 성공")
    void pushAll() {
        // given
        SearchRank java = new SearchRank("java", 1, 1);
        SearchRank react = new SearchRank("react", 2, -1);
        SearchRank spring = new SearchRank("spring", 3);

        // when
        searchRankRepository.pushAll(Arrays.asList(java, react, spring));

        // then
        assertThat(searchRankRepository.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("모든 SearchRank 를 조회한다 - 성공")
    void findAll() {
        // given
        SearchRank java = new SearchRank("java", 1, 1);
        SearchRank react = new SearchRank("react", 2, -1);
        SearchRank spring = new SearchRank("spring", 3);
        searchRankRepository.pushAll(Arrays.asList(java, react, spring));

        // when
        List<SearchRank> searchRanks = searchRankRepository.findAll();

        // then
        assertThat(searchRankRepository.count()).isEqualTo(3);
        assertThat(searchRanks).usingRecursiveComparison()
                .isEqualTo(Arrays.asList(java, react, spring));
    }
}
