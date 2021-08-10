package botobo.core.domain.tag;

import botobo.core.exception.tag.TagNameLengthException;
import botobo.core.exception.tag.TagNameNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagNameTest {

    @Test
    @DisplayName("TagName 객체 생성 - 성공")
    void create() {
        // given
        String tagNameValue = stringGenerator(20);
        TagName tagName = TagName.of(tagNameValue);

        // when, then
        assertThat(tagName).isEqualTo(TagName.of(tagNameValue));
    }

    @ValueSource(strings = {"   java", "jAvA  ", "  JAVA   "})
    @ParameterizedTest
    @DisplayName("TagName은 양 공백이 제거 후 소문자로 변경되어 생성된다.")
    void createWithTrimming(String value) {
        // given
        TagName expected = TagName.of("java");

        // when, then
        assertThat(TagName.of(value)).isEqualTo(expected);
    }

    @Test
    @DisplayName("TagName 객체 생성 - 실패, null 입력")
    void createWithNull() {
        // when, then
        assertThatThrownBy(() -> TagName.of(null))
                .isInstanceOf(TagNameNullException.class);
    }

    @EmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "\r\n", "\r"})
    @ParameterizedTest
    @DisplayName("TagName 객체 생성 - 실패, 공백 문자열 입력")
    void createWithBlank(String value) {
        // when, then
        assertThatThrownBy(() -> TagName.of(value))
                .isInstanceOf(TagNameNullException.class);
    }

    @Test
    @DisplayName("TagName 객체 생성 - 실패, 긴 문자열")
    void createWithLongString() {
        // when, then
        assertThatThrownBy(() -> TagName.of(stringGenerator(21)))
                .isInstanceOf(TagNameLengthException.class);
    }
}