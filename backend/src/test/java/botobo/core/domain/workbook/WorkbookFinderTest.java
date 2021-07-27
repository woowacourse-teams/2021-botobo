package botobo.core.domain.workbook;

import botobo.core.domain.user.User;
import botobo.core.domain.workbook.criteria.AccessType;
import botobo.core.domain.workbook.criteria.SearchKeyword;
import botobo.core.domain.workbook.criteria.WorkbookCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WorkbookFinderTest {

    private List<Workbook> workbooks;

    @BeforeEach
    void setUp() {
        workbooks = generateDummyWorkbooks();
    }

    @Test
    @DisplayName("이름에 해당 검색어를 포함한 공개 문제집만 찾는다.")
    void applyBySearch() {
        // given
        WorkbookFinder workbookFinder = WorkbookFinder.builder()
                .workbooks(workbooks)
                .build();

        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .searchKeyword(SearchKeyword.from("자바"))
                .accessType(AccessType.PUBLIC)
                .build();

        // when
        List<Workbook> workbooks = workbookFinder.apply(workbookCriteria);

        // then
        assertThat(workbooks).hasSize(2);
    }

    @Test
    @DisplayName("비공개 문제집을 찾는다.")
    void applyByAccess() {
        // given
        WorkbookFinder workbookFinder = WorkbookFinder.builder()
                .workbooks(workbooks)
                .build();

        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .accessType(AccessType.PRIVATE)
                .build();

        // when
        List<Workbook> workbooks = workbookFinder.apply(workbookCriteria);

        // then
        assertThat(workbooks).extracting("name")
                .containsExactly("데이터베이스");
    }

    private List<Workbook> generateDummyWorkbooks() {
        User adminUser = User.builder().id(1L).build();
        return Arrays.asList(
                Workbook.builder().id(1L).name("데이터베이스").opened(false).user(adminUser).build(),
                Workbook.builder().id(2L).name("자바").opened(true).user(adminUser).build(),
                Workbook.builder().id(3L).name("자바스크립트").opened(true).user(adminUser).build(),
                Workbook.builder().id(4L).name("네트워크").opened(true).user(adminUser).build(),
                Workbook.builder().id(5L).name("리액트").opened(true).user(adminUser).build(),
                Workbook.builder().id(6L).name("스프링").opened(true).user(adminUser).build()
        );
    }
}