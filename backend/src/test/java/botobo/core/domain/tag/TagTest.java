package botobo.core.domain.tag;

import botobo.core.exception.tag.TagNameNullException;
import botobo.core.exception.tag.TagNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagTest {

    @Test
    @DisplayName("Tag 객체 생성 - 성공")
    void create() {
        // given
        Tag tag = Tag.of("자바");

        // when, then
        assertThat(tag).isEqualTo(Tag.of("자바"));
    }

    @Test
    @DisplayName("Tag 객체 생성 - 실패, null 입력")
    void createWithNullTagName() {
        // when, then
        assertThatThrownBy(() -> Tag.of((TagName) null))
                .isInstanceOf(TagNullException.class);
    }

    @Test
    @DisplayName("Tag 객체 생성 - 실패, TagName null 입력")
    void createWithNullTagNameValue() {
        // when, then
        assertThatThrownBy(() -> Tag.of((String) null))
                .isInstanceOf(TagNameNullException.class);
    }
}
