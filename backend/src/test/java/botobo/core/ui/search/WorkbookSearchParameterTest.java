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
    void createOf() {
        // when, then
        assertThatCode(() -> WorkbookSearchParameter.of(
                SearchCriteria.of("date"),
                SearchKeyword.of("java"),
                0,
                10)
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("검색 요청 처리를 위한 WorkbookSearchParameter 생성 - 성공")
    void createOfRequest() {
        // when, then
        assertThatCode(() -> WorkbookSearchParameter.ofRequest(
                "date",
                "java",
                "0",
                "10")
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("검색 요청 처리를 위한 WorkbookSearchParameter 생성 - 실패, start 값이 음수인 경우")
    void createOfRequestWithNegativeStartValue() {
        // when, then
        assertThatThrownBy(() -> WorkbookSearchParameter.ofRequest(
                "date",
                "java",
                "-1",
                "10")
        ).isInstanceOf(InvalidPageStartException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "101"})
    @DisplayName("검색 요청 처리를 위한 WorkbookSearchParameter 생성 - 실패, 유효하지 않은 size 값인 경우")
    void createOfRequestWithInvalidSizeValue(String size) {
        // when, then
        assertThatThrownBy(() -> WorkbookSearchParameter.ofRequest(
                "date",
                "java",
                "0",
                size)
        ).isInstanceOf(InvalidPageSizeException.class);
    }
}
