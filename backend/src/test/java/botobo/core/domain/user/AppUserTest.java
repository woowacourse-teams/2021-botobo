package botobo.core.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppUserTest {

    @Test
    @DisplayName("익명의 AppUser 생성 - 성공")
    void createAnonymousAppUser() {
        // given
        AppUser appUser = AppUser.anonymous();

        // when, then
        assertThat(appUser.isAnonymous()).isTrue();
    }
}
