package botobo.core.domain.tag;

import botobo.core.exception.tag.InvalidTagNameException;
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
        Tag tag = Tag.from("자바");

        // when, then
        assertThat(tag).isEqualTo(Tag.from("자바"));
    }

    @Test
    @DisplayName("Tag 객체 생성 - 실패, TagName null 입력")
    void createWithNullTagName() {
        // when, then
        assertThatThrownBy(() -> Tag.from((TagName) null))
                .isInstanceOf(TagCreationFailureException.class)
                .hasMessageContaining("태그 생성시 TagName은 null이 될 수 없습니다");
    }

    @Test
    @DisplayName("Tag 객체 생성 - 실패, TagNameValue null 입력")
    void createWithNullTagNameValue() {
        // when, then
        assertThatThrownBy(() -> Tag.from((String) null))
                .isInstanceOf(InvalidTagNameException.class)
                .hasMessageContaining("null이 될 수 없습니다");
    }
}
