package botobo.core.domain.user;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.workbook.Workbook;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    @Column(nullable = false)
    private Long githubId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // TODO bio 필드 추가

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Workbook> workbooks = new ArrayList<>();

    @Builder
    public User(Long id, Long githubId, String userName, String profileUrl, Role role, List<Workbook> workbooks) {
        this.id = id;
        this.githubId = githubId;
        this.userName = userName;
        this.profileUrl = profileUrl;
        if (Objects.nonNull(role)) {
            this.role = role;
        }
        if (Objects.nonNull(workbooks)) {
            this.workbooks = workbooks;
        }
    }

    public void updateProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public AppUser toAppUser() {
        return AppUser.builder()
                .id(id)
                .role(role)
                .build();
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }

    public boolean isUser() {
        return role.isUser();
    }
}
