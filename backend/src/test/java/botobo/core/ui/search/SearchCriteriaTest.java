package botobo.core.ui.search;

import botobo.core.exception.search.InvalidSearchCriteriaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SearchCriteriaTest {

    @ParameterizedTest
    @ValueSource(strings = {"date", "name", "count", "like"})
    @DisplayName("SearchCriteria 생성 - 성공")
    void create(String value) {
        // when
        SearchCriteria searchCriteria = SearchCriteria.of(value);

        // then
        assertThat(searchCriteria.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("SearchCriteria value 가 null 일 경우 DATE 로 생성 - 성공")
    void createWithNullValue() {
        // when
        SearchCriteria searchCriteria = SearchCriteria.of(null);

        // then
        assertThat(searchCriteria).isEqualTo(SearchCriteria.DATE);
    }

    @Test
    @DisplayName("SearchCriteria 유효하지 않은 value 일 경우 - 실패")
    void createWithInvalidValue() {
        // given
        String value = "botobo";

        // when, then
        assertThatThrownBy(() -> SearchCriteria.of(value))
                .isInstanceOf(InvalidSearchCriteriaException.class);
    }
}