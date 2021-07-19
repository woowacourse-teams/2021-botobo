package botobo.core.quiz.domain.workbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static botobo.core.TestUtils.longStringGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

class WorkbookTest {

    @Test
    @DisplayName("Builder를 이용한 Workbook 객체 생성 - 성공")
    void createWithBuilder() {
        //given
        String workbookName = "단계별 자바 문제집";

        // when
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .build();

        // then
        assertThat(workbook.getName()).isEqualTo(workbookName);
        assertFalse(workbook.isDeleted());
        assertFalse(workbook.isPublic());
    }

    @Test
    @DisplayName("Builder를 이용한 Workbook 객체 생성 - 성공, 유효한 길이의 이름")
    void createWithValidLengthName() {
        // when, then
        assertThatCode(() -> Workbook.builder()
                .name(longStringGenerator(30))
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Builder를 이용한 Workbook 객체 생성 - 실패, 긴 길이의 이름")
    void createWithLongName() {
        // whenm, then
        assertThatThrownBy(() -> Workbook.builder()
                .name(longStringGenerator(31))
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @NullAndEmptySource
    @ParameterizedTest
    @ValueSource(strings = {" ", "    "})
    @DisplayName("Builder를 이용한 Workbook 생성 - 실패, name은 null, empty or blank String이 될 수 없다.")
    void createWithInvalidName(String name) {
        // when, then
        assertThatThrownBy(() -> Workbook.builder()
                .name(name)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

}