package botobo.core.ui.search;

import botobo.core.exception.search.InvalidPageableException;
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
                .searchType("name")
                .searchKeyword("java")
                .searchCriteria("date")
                .searchOrder("desc")
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
                .searchType("name")
                .searchKeyword("java")
                .searchCriteria("date")
                .searchOrder("desc")
                .start("-1")
                .size("10")
                .build()
        ).isInstanceOf(InvalidPageableException.class)
                .hasMessage("페이지의 시작 값은 음수가 될 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "101"})
    @DisplayName("WorkbookSearchParameter 생성 - 실패, 유효하지 않은 size 값인 경우")
    void createWithInvalidSizeValue(String size) {
        // when, then
        assertThatThrownBy(() -> WorkbookSearchParameter.builder()
                .searchType("name")
                .searchKeyword("java")
                .searchCriteria("date")
                .searchOrder("desc")
                .start("0")
                .size(size)
                .build()
        ).isInstanceOf(InvalidPageableException.class)
                .hasMessage("유효하지 않은 페이지 크기입니다. 유효한 크기 : 1 ~ 100");
    }
}