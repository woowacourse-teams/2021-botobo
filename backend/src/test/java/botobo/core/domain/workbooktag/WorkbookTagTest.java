package botobo.core.domain.workbooktag;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagName;
import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.workbooktag.WorkbookTagCreationFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkbookTagTest {

    private Workbook workbook;
    private Tag tag;

    @BeforeEach
    void setUp() {
        workbook = Workbook.builder()
                .name("자바 문제 모음")
                .build();
        tag = Tag.from(TagName.from("자바"));
    }

    @Test
    @DisplayName("WorkbookTag 객체 생성 - 성공")
    void create() {
        // when
        WorkbookTag workbookTag = WorkbookTag.of(workbook, tag);

        // when, then
        assertThat(workbookTag.getWorkbook()).isEqualTo(workbook);
        assertThat(workbookTag.getTag()).isEqualTo(tag);
    }

    @Test
    @DisplayName("WorkbookTag 객체 생성 - 실패, Workbook에 null 입력")
    void createWithNullWorkbook() {
        // when, then
        assertThatThrownBy(() -> WorkbookTag.of(null, tag))
                .isInstanceOf(WorkbookTagCreationFailureException.class)
                .hasMessageContaining("WorkbookTag 생성시 Workbook은 null이 될 수 없습니다");
    }

    @Test
    @DisplayName("WorkbookTag 객체 생성 - 실패, Tag에 null 입력")
    void createWithNullTag() {
        // when, then
        assertThatThrownBy(() -> WorkbookTag.of(workbook, null))
                .isInstanceOf(WorkbookTagCreationFailureException.class)
                .hasMessageContaining("WorkbookTag 생성시 Tag는 null이 될 수 없습니다");
    }
}
