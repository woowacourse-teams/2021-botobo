package botobo.core.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
}
