package botobo.core.domain.tag;

import botobo.core.exception.tag.TagCreationFailureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagTest {

    @Test
    @DisplayName("Tag 객체 생성 - 성공")
    void create() {
        // given
        Tag tag = Tag.from(TagName.from("자바"));

        // when, then
        assertThat(tag).isEqualTo(Tag.from(TagName.from("자바")));
    }

    @Test
    @DisplayName("Tag 객체 생성 - 실패, null 입력")
    void createWithNull() {
        // when, then
        assertThatThrownBy(() -> Tag.from(null))
                .isInstanceOf(TagCreationFailureException.class)
                .hasMessageContaining("태그 생성시 TagName은 null이 될 수 없습니다");
    }
}
