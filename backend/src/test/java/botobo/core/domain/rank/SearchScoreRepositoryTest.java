package botobo.core.domain.rank;

import botobo.core.config.redis.EmbeddedRedisConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@MockBean(JpaMetamodelMappingContext.class)
@ActiveProfiles("test")
@Import({EmbeddedRedisConfig.class, SearchScoreRepository.class})
class SearchScoreRepositoryTest {

    private static final String JAVA = "java";

    @Autowired
    private SearchScoreRepository searchScoreRepository;

    @AfterEach
    void tearDown() {
        searchScoreRepository.deleteAll();
    }

    @Test
    @DisplayName("검색 키워드의 스코어를 가져온다 - 성공, 검색 키워드가 없으면 null")
    void scoreWithNotStoredValue() {
        assertThat(searchScoreRepository.score(JAVA)).isNull();
    }

    @Test
    @DisplayName("검색 키워드의 스코어를 1점 증가시킨다 - 성공, 키워드가 없으면 0점에서 더해져 1점이 된다")
    void increaseScoreWithNotStoredValue() {
        // when
        searchScoreRepository.increaseScore(JAVA);

        // then
        assertThat(searchScoreRepository.score(JAVA)).isEqualTo(1.0);
    }

    @Test
    @DisplayName("한 번이라도 저장된 키워드의 수를 가져온다 - 성공")
    void count() {
        // given
        searchScoreRepository.increaseScore("apple");
        searchScoreRepository.increaseScore("banana");
        searchScoreRepository.increaseScore("cherry");

        // when, then
        assertThat(searchScoreRepository.count()).isEqualTo(3L);
    }

    @Test
    @DisplayName("모든 키워드를 지운다 - 성공")
    void removeALl() {
        // given
        searchScoreRepository.increaseScore("apple");
        searchScoreRepository.increaseScore("banana");
        searchScoreRepository.increaseScore("cherry");
        assertThat(searchScoreRepository.count()).isEqualTo(3L);

        // when
        searchScoreRepository.deleteAll();

        // then
        assertThat(searchScoreRepository.count()).isZero();
    }

    @Test
    @DisplayName("점수가 높은 순으로 키워드를 가져온다 - 성공")
    void findByScoreDesc() {
        // given
        setScore("pk", 10);
        setScore("bear", 1);
        setScore("joanne", 7);
        setScore("oz", 5);
        setScore("kyle", 9);
        setScore("ditto", 3);

        // when
        Set<String> searchRank = searchScoreRepository.findByScoreDesc(3);

        // then
        assertThat(searchRank).containsExactly("pk", "kyle", "joanne");
    }

    @Test
    @DisplayName("limitCount 보다 적은 데이터를 가지고 있는 경우 점수가 높은 순으로 모든 키워드를 가져온다 - 성공")
    void findByScoreDescWhenLessThanLimitCount() {
        // given
        setScore("pk", 10);
        setScore("bear", 1);

        // when
        Set<String> searchRank = searchScoreRepository.findByScoreDesc(3);

        // then
        assertThat(searchRank).containsExactly("pk", "bear");
    }

    private void setScore(String searchKeyword, int score) {
        for (int i = 0; i < score; i++) {
            searchScoreRepository.increaseScore(searchKeyword);
        }
    }
}
