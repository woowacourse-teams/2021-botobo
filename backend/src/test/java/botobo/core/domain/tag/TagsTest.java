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
        Tags tags = Tags.of(Arrays.asList(
                Tag.of("자바"), Tag.of("스프링")
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
        assertThatThrownBy(() -> Tags.of((List<Tag>) null))
                .isInstanceOf(TagsCreationFailureException.class);
    }

    @Test
    @DisplayName("태그 이름의 교집합 검사 - 성공, 두 Tags에서 같은 이름을 가지는 태그의 수를 구한다.")
    void countSameTagName() {
        // given
        Tags tags = Tags.of(Arrays.asList(
                Tag.of("자바"), Tag.of("스프링"), Tag.of("리액트")
        ));

        Tags others = Tags.of(Arrays.asList(
                Tag.of("자바"), Tag.of("스프링"), Tag.of("자바스크립트")
        ));

        // when, then
        assertThat(tags.countSameTagName(others)).isEqualTo(2);
    }
}