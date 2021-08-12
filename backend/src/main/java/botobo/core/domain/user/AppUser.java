package botobo.core.domain.user;

import botobo.core.exception.user.AnonymousHasNotIdException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    private Long id;
    private Role role;

    public static AppUser anonymous() {
        return AppUser.builder()
                .role(Role.ANONYMOUS)
                .build();
    }

    public static AppUser admin(Long userId) {
        return AppUser.builder()
                .id(userId)
                .role(Role.ADMIN)
                .build();
    }

    public static AppUser user(Long userId) {
        return AppUser.builder()
                .id(userId)
                .role(Role.USER)
                .build();
    }

    public boolean isAnonymous() {
        return role.isAnonymous();
    }

    public long getId() {
        if (isAnonymous()) {
            throw new AnonymousHasNotIdException("비회원의 ID는 조회할 수 없습니다.");
        }
        return id;
    }

    public Role getRole() {
        return role;
    }
}
