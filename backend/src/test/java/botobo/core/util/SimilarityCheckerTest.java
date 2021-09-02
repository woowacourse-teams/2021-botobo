package botobo.core.util;

import botobo.core.domain.tag.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimilarityCheckerTest {

    @DisplayName("유사도 순으로 정렬된 태그 5개를 반환한다. - 성공, 한글")
    @Test
    void orderBySimilarity() {
        // given
        List<Tag> tags = List.of(
                Tag.of("나무자바"),
                Tag.of("자바스크립트 문제집"),
                Tag.of("자바스"),
                Tag.of("자바리자바"),
                Tag.of("자바"),
                Tag.of("자바스크립트"),
                Tag.of("자바를 잡아라"),
                Tag.of("공자바")
        );

        // when
        List<Tag> orderBySimilarity = SimilarityChecker.orderBySimilarity("자바", tags, 5);

        // then
        assertThat(orderBySimilarity).containsExactly(
                Tag.of("자바"), Tag.of("자바스"), Tag.of("공자바"), Tag.of("나무자바"), Tag.of("자바리자바")
        ).hasSize(5);
    }

    @DisplayName("유사도 순으로 정렬된 태그 5개를 반환한다. - 성공, 영어")
    @Test
    void orderBySimilarityEng() {
        // given
        List<Tag> tags = List.of(
                Tag.of("java"),
                Tag.of("javascript"),
                Tag.of("javajigi"),
                Tag.of("javabom"),
                Tag.of("aajava"),
                Tag.of("bbjava"),
                Tag.of("ozjava"),
                Tag.of("javajoanne")
        );

        // when
        List<Tag> orderBySimilarity = SimilarityChecker.orderBySimilarity("자바", tags, 5);

        // then
        assertThat(orderBySimilarity).containsExactly(
                Tag.of("java"), Tag.of("aajava"), Tag.of("bbjava"), Tag.of("ozjava"), Tag.of("javabom")
        ).hasSize(5);
    }
}