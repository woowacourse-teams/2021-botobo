package botobo.core.ui.search;

import botobo.core.exception.search.InvalidPageSizeException;
import botobo.core.exception.search.InvalidPageStartException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkbookSearchParameterTest {

    @Test
    @DisplayName("WorkbookSearchParameter 생성 - 성공")
    void create() {
        // when, then
        assertThatCode(() -> WorkbookSearchParameter.builder()
                .searchKeyword("java")
                .searchCriteria("date")
                .start("0")
                .size("10")
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("WorkbookSearchParameter 생성 - 실패, start 값이 음수인 경우")
    void createWithNegativeStartValue() {
        // when, then
        assertThatThrownBy(() -> WorkbookSearchParameter.builder()
                .searchKeyword("java")
                .searchCriteria("date")
                .start("-1")
                .size("10")
                .build()
        ).isInstanceOf(InvalidPageStartException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "101"})
    @DisplayName("WorkbookSearchParameter 생성 - 실패, 유효하지 않은 size 값인 경우")
    void createWithInvalidSizeValue(String size) {
        // when, then
        assertThatThrownBy(() -> WorkbookSearchParameter.builder()
                .searchKeyword("java")
                .searchCriteria("date")
                .start("0")
                .size(size)
                .build()
        ).isInstanceOf(InvalidPageSizeException.class);
    }
}