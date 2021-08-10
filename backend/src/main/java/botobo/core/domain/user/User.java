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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String profileUrl;

    @Column(nullable = false)
    private String bio = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    // TODO bio 필드 추가

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Workbook> workbooks = new ArrayList<>();

    @Builder
    public User(Long id, String socialId, String userName, String profileUrl, Role role, List<Workbook> workbooks, SocialType socialType) {
        this.id = id;
        this.socialId = socialId;
        this.userName = userName;
        this.profileUrl = profileUrl;
        if (Objects.nonNull(bio)) {
            this.bio = bio;
        }
        if (Objects.nonNull(role)) {
            this.role = role;
        }
        if (Objects.nonNull(workbooks)) {
            this.workbooks = workbooks;
        }
        this.socialType = socialType;
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

    public void updateProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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

    public boolean isSameName(User other) {
        return Objects.equals(this.userName, other.getUserName());
    }
}
