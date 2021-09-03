package botobo.core.dto.tag;

import botobo.core.exception.workbook.WorkbookNameLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class WorkbookNameTest {

    @DisplayName("workbook이 비어있으면 true를 리턴한다. - 성공")
    @Test
    void isEmpty() {
        // given
        WorkbookName workbookName = new WorkbookName("");
        // when - then
        assertThat(workbookName.isEmpty()).isTrue();
    }

    @DisplayName("workbook을 생성한다. - 성공, 항상 소문자를 갖는다.")
    @Test
    void create() {
        // given
        WorkbookName workbookName = new WorkbookName("JAVA");
        // when - then
        assertThat(workbookName.getWorkbook()).isEqualTo("java");
    }

    @DisplayName("workbook을 생성한다. - 성공, 비어있거나 null이면 빈 문자열을 가진다.")
    @ParameterizedTest
    @NullAndEmptySource
    void create(String workbook) {
        // given
        WorkbookName workbookName = new WorkbookName(workbook);
        // when - then
        assertThat(workbookName.getWorkbook()).isEqualTo("");
    }

    @DisplayName("workbook을 생성한다. - 실패, 길이가 30자를 넘으면 예외")
    @Test
    void createFailed() {
        // given
        assertThatThrownBy(() -> new WorkbookName(stringGenerator(31)))
                .isInstanceOf(WorkbookNameLengthException.class);
    }
}
