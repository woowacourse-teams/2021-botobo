package botobo.core.ui.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class SearchRelatedTest {

    @DisplayName("value로 SearchRelated를 생성한다. - 성공")
    @Test
    void create() {
        // given
        assertThatCode(() -> new SearchRelated("value"))
                .doesNotThrowAnyException();
    }

    @DisplayName("value를 가져온다. - 성공, 알맞은 value")
    @Test
    void get() {
        // given
        SearchRelated searchRelated = new SearchRelated("java");
        // when - then
        assertThat(searchRelated.get()).isEqualTo("java");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("value를 가져온다. - 성공, 올바르지 않은 value의 경우 빈 문자열 반환")
    void getWhenNull(String value) {
        // given
        SearchRelated searchRelated = new SearchRelated(value);
        // when - then
        assertThat(searchRelated.get()).isEqualTo("");
    }

    @ParameterizedTest
    @ValueSource(strings = {"JAVA", "JaVa", "java", "jAvA"})
    @DisplayName("value를 가져온다. - 성공, 항상 소문자를 반환")
    void getAlwaysLowerCase(String value) {
        // given
        SearchRelated searchRelated = new SearchRelated(value);
        // when - then
        assertThat(searchRelated.get()).isEqualTo("java");
    }

    @ParameterizedTest
    @ValueSource(strings = {" java ", " JAVA ", " hi java "})
    @DisplayName("value를 가져온다. - 성공, 양 끝 빈 문자열은 trim한다.")
    void getAlwaysTrim(String value) {
        // given
        SearchRelated searchRelated = new SearchRelated(value);
        // when - then
        assertThat(searchRelated.get()).isEqualTo(value.toLowerCase().trim());
    }
}