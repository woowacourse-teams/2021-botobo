package botobo.core.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TagNamesTest {

    @Test
    @DisplayName("TagNames 객체 생성 - 성공")
    void create() {
        // given
        List<String> names = Arrays.asList("자바", "자바스크립트", "리액트");
        TagNames tagNames = TagNames.from(names);

        // when, then
        assertThat(tagNames.toList())
                .extracting("value")
                .containsExactly("자바", "자바스크립트", "리액트");
    }

    @Test
    @DisplayName("TagNames 객체 생성시 중복 이름은 제거된다.")
    void createDistinctly() {
        // given
        List<String> names = Arrays.asList("자바", "자바스크립트", "리액트", "자바", "자바");
        TagNames tagNames = TagNames.from(names);

        // when, then
        assertThat(tagNames.toList()).hasSize(3);
    }
}