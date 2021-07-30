package botobo.core.domain.tag;

import botobo.core.exception.tag.TagNamesCreationFailureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagNamesTest {

    @Test
    @DisplayName("TagNames 객체 생성 - 성공")
    void create() {
        // given
        List<String> names = Arrays.asList("자바", "자바스크립트", "리액트");

        // when
        TagNames tagNames = TagNames.from(names);

        // then
        assertThat(tagNames.toList())
                .extracting("value")
                .containsExactly("자바", "자바스크립트", "리액트");
    }

    @Test
    @DisplayName("TagNames 객체 생성 - 실패, null 입력")
    void createWithNull() {
        // when, then
        assertThatThrownBy(() -> TagNames.from(null))
                .isInstanceOf(TagNamesCreationFailureException.class)
                .hasMessageContaining("TagNames객체 생성에 실패했습니다")
                .hasMessageContaining("인자는 null이 될 수 없습니다");
    }

    @Test
    @DisplayName("TagNames 객체 생성시 중복 이름은 제거된다.")
    void createDistinctly() {
        // given
        List<String> names = Arrays.asList("자바", "자바스크립트", "리액트", "자바", "자바");

        // when
        TagNames tagNames = TagNames.from(names);

        // then
        assertThat(tagNames.toList()).hasSize(3);
    }
}