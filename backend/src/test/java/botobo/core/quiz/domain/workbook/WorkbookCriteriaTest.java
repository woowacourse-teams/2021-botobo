package botobo.core.quiz.domain.workbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class WorkbookCriteriaTest {

    @Test
    @DisplayName("WorkbookCriteria 객체 생성 - 성공")
    void create() {
        // when
        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .keyword("java")
                .opened("false")
                .build();

        //then
        assertThat(workbookCriteria.getSearchKeyword()).isEqualTo(SearchKeyword.from("java"));
        assertThat(workbookCriteria.isOpened()).isFalse();
    }

    @Test
    @DisplayName("WorkbookCriteria 객체 생성 시 인자가 없으면 키워드 없음, 공개로 생성된다.")
    void createWithNoParams() {
        // when
        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .build();

        //then
        assertThat(workbookCriteria.isNoSearchKeyword()).isTrue();
        assertThat(workbookCriteria.isOpened()).isTrue();
    }

    @ValueSource(strings = {"false", "False", "FALSE", "fAlSe"})
    @ParameterizedTest
    @DisplayName("WorkbookCriteria 객체 생성 시 opened 필드의 값이 대소문자 구분 없이 false 이면 비공개로 생성된다.")
    void createWithOpenedFalse(String opened) {
        // when
        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .opened(opened)
                .build();

        //then
        assertThat(workbookCriteria.isOpened()).isFalse();
    }

    @ValueSource(strings = {"true", "TRUE", "  ", "ffalse", "anything value"})
    @ParameterizedTest
    @DisplayName("WorkbookCriteria 객체 생성 시 opened 필드의 값이 false가 아니면 공개로 생성된다.")
    void createWithOpenedOther(String opened) {
        // when
        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .opened(opened)
                .build();

        //then
        assertThat(workbookCriteria.isOpened()).isTrue();
    }
}