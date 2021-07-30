package botobo.core.domain.tag;

import botobo.core.exception.tag.TagsCreationFailureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagsTest {

    @Test
    @DisplayName("Tags 객체 생성 - 성공")
    void create() {
        // when
        Tags tags = Tags.from(Arrays.asList(
                Tag.from("자바"), Tag.from("스프링")
        ));

        // then
        assertThat(tags.toList()).extracting("tagName")
                .extracting("value")
                .containsExactly("자바", "스프링");
    }

    @Test
    @DisplayName("Tags 객체 생성 - 실패, null 입력")
    void createWithNull() {
        // when, then
        assertThatThrownBy(() -> Tags.from(null))
                .isInstanceOf(TagsCreationFailureException.class)
                .hasMessageContaining("Tags객체 생성에 실패했습니다")
                .hasMessageContaining("tags는 null이 될 수 없습니다");
    }

    @Test
    @DisplayName("Tags 객체 생성 - 실패, 중복된 태그이름 입력")
    void createWithNullTagNameValue() {
        // given
        List<Tag> tags = Arrays.asList(
                Tag.from("자바"), Tag.from("스프링"), Tag.from("자바")
        );

        // when, then
        assertThatThrownBy(() -> Tags.from(tags))
                .isInstanceOf(TagsCreationFailureException.class)
                .hasMessageContaining("Tags객체 생성에 실패했습니다")
                .hasMessageContaining("중복된 태그 이름이 있습니다");
    }

    @Test
    @DisplayName("두 Tags에서 같은 이름을 가지는 태그의 수를 구한다.")
    void countSameTagName() {
        // given
        Tags tags = Tags.from(Arrays.asList(
                Tag.from("자바"), Tag.from("스프링"), Tag.from("리액트")
        ));

        Tags others = Tags.from(Arrays.asList(
                Tag.from("자바"), Tag.from("스프링"), Tag.from("자바스크립트")
        ));

        // when, then
        assertThat(tags.countSameTagName(others)).isEqualTo(2);
    }
}