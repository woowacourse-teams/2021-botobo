package botobo.core.ui.search;

import botobo.core.exception.search.InvalidSearchOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SearchOrderTest {

    @ParameterizedTest
    @ValueSource(strings = {"asc", "desc"})
    @DisplayName("SearchOrder 생성 - 성공")
    void create(String value) {
        // when
        SearchOrder searchOrder = SearchOrder.of(value);

        // then
        assertThat(searchOrder.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("SearchOrder value 가 null 일 경우 DATE 로 생성 - 성공")
    void createWithNullValue() {
        // when
        SearchOrder searchOrder = SearchOrder.of(null);

        // then
        assertThat(searchOrder).isEqualTo(SearchOrder.DESC);
    }

    @Test
    @DisplayName("SearchOrder 유효하지 않은 value 일 경우 - 실패")
    void createWithInvalidValue() {
        // given
        String value = "botobo";

        // when, then
        assertThatThrownBy(() -> SearchOrder.of(value))
                .isInstanceOf(InvalidSearchOrderException.class);
    }
}