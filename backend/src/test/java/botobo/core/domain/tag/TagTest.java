package botobo.core.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagTest {

    @Test
    @DisplayName("Tag 객체 생성 - 성공")
    void create() {
        // given
        Tag tag = Tag.from(TagName.from("자바"));

        // when, then
        assertThat(tag).isEqualTo(Tag.from(TagName.from("자바")));
    }
}
