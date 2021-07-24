package botobo.core.user.domain;

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

    public boolean isAnonymous() {
        return role.isAnonymous();
    }
}