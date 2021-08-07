package botobo.core.domain.heart;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.heart.HeartCreationFailureException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Heart extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false, foreignKey = @ForeignKey(name = "FK_like_to_workbook"))
    private Workbook workbook;

    private Long userId;

    @Builder
    public Heart(Long id, Workbook workbook, Long userId) {
        validateNotNull(workbook, userId);
        this.id = id;
        this.workbook = workbook;
        this.userId = userId;
    }

    private void validateNotNull(Workbook workbook, Long userId) {
        if (Objects.isNull(workbook)) {
             throw new HeartCreationFailureException("문제집 필요");
        }
        if (Objects.isNull(userId)) {
            throw new HeartCreationFailureException("유저 아이디 필요");
        }
    }

    public boolean ownedBy(Long userId) {
        return this.userId.equals(userId);
    }
}
