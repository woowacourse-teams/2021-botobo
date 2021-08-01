package botobo.core.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AppUserTest {

    @Test
    @DisplayName("익명의 AppUser 생성 - 성공")
    void createAnonymousAppUser() {
        // given
        AppUser appUser = AppUser.anonymous();

        // when, then
        assertThat(appUser.isAnonymous()).isTrue();
    }

    @Test
    @DisplayName("익명 유저의 id 조회 - 실패, 익명 유저는 id를 조회할 수 없음.")
    void getIdFromAnonymousUser() {
        // given
        AppUser anonymous = AppUser.anonymous();

        // when, then
        assertThatThrownBy(anonymous::getId).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("AppUser의 id 조회 - 성공, 익명 유저를 제외한 AppUser는 ID 조회가 가능하다.")
    void getIdExcludeAnonymousUser() {
        // given
        AppUser user = AppUser.user(1L);
        AppUser admin = AppUser.admin(1L);

        // when, then
        assertThatCode(user::getId).doesNotThrowAnyException();
        assertThatCode(admin::getId).doesNotThrowAnyException();
    }
}
