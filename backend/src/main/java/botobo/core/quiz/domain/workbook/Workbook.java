package botobo.core.quiz.domain.workbook;

import botobo.core.quiz.domain.BaseEntity;
import botobo.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;


@Getter
@NoArgsConstructor
@Entity
public class Workbook extends BaseEntity {

    private static final int NAME_MAX_LENGTH = 30;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_workbook_to_user"))
    private User user;

    @Builder
    public Workbook(String name, boolean isDeleted, boolean isPublic, User user) {
        validateName(name);
        this.name = name;
        this.isDeleted = isDeleted;
        this.isPublic = isPublic;
        this.user = user;
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("Workbook의 Name에는 null이 들어갈 수 없습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Workbook의 Name에는 비어있거나 공백 문자열이 들어갈 수 없습니다.");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Workbook의 Name %d자 이하여야 합니다.", NAME_MAX_LENGTH)
            );
        }
    }
}
