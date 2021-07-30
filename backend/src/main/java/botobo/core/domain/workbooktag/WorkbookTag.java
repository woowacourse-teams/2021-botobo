package botobo.core.domain.workbooktag;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.workbooktag.WorkbookTagCreationFailureException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class WorkbookTag extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false, foreignKey = @ForeignKey(name = "FK_workbooktag_to_workbook"))
    private Workbook workbook;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tag_id", nullable = false, foreignKey = @ForeignKey(name = "FK_workbooktag_to_tag"))
    private Tag tag;

    private WorkbookTag(Workbook workbook, Tag tag) {
        validateNotNull(workbook, tag);
        this.workbook = workbook;
        this.tag = tag;
    }

    private void validateNotNull(Workbook workbook, Tag tag) {
        if (Objects.isNull(workbook)) {
            throw new WorkbookTagCreationFailureException("WorkbookTag 생성시 Workbook은 null이 될 수 없습니다.");
        }
        if (Objects.isNull(tag)) {
            throw new WorkbookTagCreationFailureException("WorkbookTag 생성시 Tag는 null이 될 수 없습니다.");
        }
    }

    public static WorkbookTag of(Workbook workbook, Tag tag) {
        return new WorkbookTag(workbook, tag);
    }
}
