package botobo.core.dto.tag;

import botobo.core.exception.workbook.WorkbookNameLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FilterCriteriaTest {

    @DisplayName("workbook이 비어있으면 true를 리턴한다. - 성공")
    @Test
    void isEmpty() {
        // given
        FilterCriteria filterCriteria = new FilterCriteria("");
        // when - then
        assertThat(filterCriteria.isEmpty()).isTrue();
    }

    @DisplayName("workbook을 생성한다. - 성공, 항상 소문자를 갖는다.")
    @Test
    void create() {
        // given
        FilterCriteria filterCriteria = new FilterCriteria("JAVA");
        // when - then
        assertThat(filterCriteria.getWorkbook()).isEqualTo("java");
    }

    @DisplayName("workbook을 생성한다. - 성공, 비어있거나 null이면 빈 문자열을 가진다.")
    @ParameterizedTest
    @NullAndEmptySource
    void create(String workbook) {
        // given
        FilterCriteria filterCriteria = new FilterCriteria(workbook);
        // when - then
        assertThat(filterCriteria.getWorkbook()).isEqualTo("");
    }

    @DisplayName("workbook을 생성한다. - 실패, 길이가 30자를 넘으면 예외")
    @Test
    void createFailed() {
        // given
        assertThatThrownBy(() -> new FilterCriteria(stringGenerator(31)))
                .isInstanceOf(WorkbookNameLengthException.class);
    }
}
