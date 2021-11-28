package botobo.core.util;

import botobo.core.domain.tag.dto.TagDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SimilarityCheckerTest {

    @DisplayName("유사도 순으로 정렬된 태그 5개를 반환한다. - 성공, 한글")
    @Test
    void orderBySimilarity() {
        // given
        List<TagDto> tags = List.of(
                TagDto.of(1L, "나무자바"),
                TagDto.of(2L, "자바스크립트 문제집"),
                TagDto.of(3L, "자바스"),
                TagDto.of(4L, "자바리자바"),
                TagDto.of(5L, "자바"),
                TagDto.of(6L, "자바스크립트"),
                TagDto.of(7L, "자바를 잡아라"),
                TagDto.of(8L, "공자바")
        );

        // when
        List<TagDto> orderBySimilarity = SimilarityChecker.orderBySimilarity("자바", tags, 5);

        // then
        assertThat(orderBySimilarity).hasSize(5);
        assertThat(orderBySimilarity.stream()
                .map(TagDto::getName)
                .collect(Collectors.toList()))
                .containsExactly("자바", "자바스", "자바리자바", "자바스크립트", "자바를 잡아라");
    }

    @DisplayName("유사도 순으로 정렬된 태그 5개를 반환한다. - 성공, 영어")
    @Test
    void orderBySimilarityEng() {
        // given
        List<TagDto> tags = List.of(
                TagDto.of(1L, "java"),
                TagDto.of(2L, "javascript"),
                TagDto.of(3L, "java11"),
                TagDto.of(4L, "aajava"),
                TagDto.of(5L, "bbjava"),
                TagDto.of(6L, "ozjava"),
                TagDto.of(7L, "javajoanne")
        );

        // when
        List<TagDto> orderBySimilarity = SimilarityChecker.orderBySimilarity("java", tags, 5);
        // then
        assertThat(orderBySimilarity).hasSize(5);
        assertThat(orderBySimilarity.stream()
                .map(TagDto::getName)
                .collect(Collectors.toList()))
                .containsExactly("java", "java11", "javajoanne", "javascript", "aajava");
    }
}
