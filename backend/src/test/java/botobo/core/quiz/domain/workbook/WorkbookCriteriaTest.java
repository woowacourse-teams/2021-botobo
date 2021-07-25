package botobo.core.quiz.domain.workbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WorkbookCriteriaTest {

    @Test
    @DisplayName("WorkbookCriteria 객체 생성 - 성공")
    void create() {
        // when
        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .keyword("java")
                .access("private")
                .build();

        //then
        assertThat(workbookCriteria.getSearchKeyword()).isEqualTo(SearchKeyword.from("java"));
        assertThat(workbookCriteria.isPrivateAccess()).isTrue();
    }

    @Test
    @DisplayName("WorkbookCriteria 객체 생성 시 인자가 없으면 키워드 없음, 공개, 모든 사람의 문제집으로 생성된다.")
    void createWithNoParams() {
        // when
        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .build();

        //then
        assertThat(workbookCriteria.isNoSearchKeyword()).isTrue();
        assertThat(workbookCriteria.isPublicAccess()).isTrue();
        assertThat(workbookCriteria.isMineType()).isFalse();
    }
}