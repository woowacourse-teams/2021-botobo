package botobo.core.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @DisplayName("익명의 유저를 테스트한다.")
    @Test
    void isAnonymous() {
        // given
        Role anonymous = Role.ANONYMOUS;

        // when, then
        assertThat(anonymous.isAnonymous()).isTrue();
    }

    @DisplayName("어드민 유저를 테스트한다.")
    @Test
    void isAdmin() {
        // given
        Role admin = Role.ADMIN;

        // when, then
        assertThat(admin.isAdmin()).isTrue();
    }

    @DisplayName("유저를 테스트한다.")
    @Test
    void isUser() {
        // given
        Role user = Role.USER;

        // when, then
        assertThat(user.isUser()).isTrue();
    }
}
