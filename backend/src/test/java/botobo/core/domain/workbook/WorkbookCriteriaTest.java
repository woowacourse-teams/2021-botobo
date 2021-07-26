package botobo.core.domain.workbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WorkbookCriteriaTest {

    @Test
    @DisplayName("WorkbookCriteria 객체 생성 - 성공")
    void create() {
        // when
        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .searchKeyword(SearchKeyword.from("java"))
                .accessType(AccessType.PRIVATE)
                .build();

        //then
        assertThat(workbookCriteria.getSearchKeyword()).isEqualTo(SearchKeyword.from("java"));
        assertThat(workbookCriteria.isPrivateAccess()).isTrue();
    }

    @Test
    @DisplayName("WorkbookCriteria 객체 생성 시 인자가 없으면 키워드 없음, 공개로 생성된다.")
    void createWithNoParams() {
        // when
        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .build();

        //then
        assertThat(workbookCriteria.isNoSearchKeyword()).isTrue();
        assertThat(workbookCriteria.isPublicAccess()).isTrue();
    }
}