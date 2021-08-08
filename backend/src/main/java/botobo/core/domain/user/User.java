package botobo.core.domain.user;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.user.ProfileUpdateNotAllowedException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
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

    private String bio = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Workbook> workbooks = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(bio)) {
            this.bio = "";
        }
    }

    @Builder
    public User(Long id, Long githubId, String userName, String profileUrl, String bio, Role role, List<Workbook> workbooks) {
        this.id = id;
        this.githubId = githubId;
        this.userName = userName;
        this.profileUrl = profileUrl;
        this.bio = bio;
        if (Objects.nonNull(role)) {
            this.role = role;
        }
        if (Objects.nonNull(workbooks)) {
            this.workbooks = workbooks;
        }
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

    public void update(User other) {
        validateProfileUrl(other.getProfileUrl());
        this.userName = other.getUserName();
        this.bio = other.getBio();
    }

    private void validateProfileUrl(String profileUrl) {
        if (!Objects.equals(this.profileUrl, profileUrl)) {
            throw new ProfileUpdateNotAllowedException();
        }
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }
}
