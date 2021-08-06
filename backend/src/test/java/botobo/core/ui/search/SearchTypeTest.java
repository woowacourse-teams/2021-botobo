package botobo.core.ui.search;

import botobo.core.exception.search.InvalidSearchTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SearchTypeTest {

    @ParameterizedTest
    @ValueSource(strings = {"name", "tag", "user"})
    @DisplayName("SearchType 생성 - 성공")
    void create(String value) {
        // when
        SearchType searchType = SearchType.of(value);

        // then
        assertThat(searchType.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("SearchType value 가 null 일 경우 NAME 으로 생성 - 성공")
    void createWithNullValue() {
        // when
        SearchType searchType = SearchType.of(null);

        // then
        assertThat(searchType).isEqualTo(SearchType.NAME);
    }

    @Test
    @DisplayName("SearchType 유효하지 않은 value 일 경우 - 실패")
    void createWithInvalidValue() {
        // given
        String value = "botobo";

        // when, then
        assertThatThrownBy(() -> SearchType.of(value))
                .isInstanceOf(InvalidSearchTypeException.class);
    }
}