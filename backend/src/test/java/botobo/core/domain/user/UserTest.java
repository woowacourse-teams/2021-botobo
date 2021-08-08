package botobo.core.domain.user;

import botobo.core.exception.user.ProfileUpdateNotAllowedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserTest {

    @Test
    @DisplayName("Builder를 이용한 User 객체 생성 - 성공")
    void createWithBuilder() {
        assertThatCode(() -> User.builder()
                .id(1L)
                .githubId(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Builder를 이용한 User 객체 생성 - 성공, bio는 ''를 디폴트로 한다.")
    void createUserWithoutBio() {
        User user = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build();
        assertThat(user.getBio()).isEqualTo("");
    }

    @Test
    @DisplayName("Bio를 포함한 User 객체 생성 - 성공")
    void createUserWithBio() {
        User user = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("user")
                .bio("백엔드 개발자 유저입니다.")
                .profileUrl("profile.io")
                .build();
        assertThat(user.getBio()).isEqualTo("백엔드 개발자 유저입니다.");
    }

    @Test
    @DisplayName("유저의 정보를 업데이트 한다. - 성공, profileUrl 제외 업데이트")
    void update() {
        User user = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("조앤")
                .bio("백엔드 개발자")
                .profileUrl("profile.io")
                .build();

        User updateUser = User.builder()
                .id(1L)
                .userName("카일")
                .bio("프론트엔드 개발자")
                .profileUrl("profile.io")
                .build();

        user.update(updateUser);

        assertAll(
                () -> assertThat(user.getUserName()).isEqualTo("카일"),
                () -> assertThat(user.getBio()).isEqualTo("프론트엔드 개발자"),
                () -> assertThat(user.getProfileUrl()).isEqualTo("profile.io")
        );
    }

    @Test
    @DisplayName("유저의 정보를 업데이트 한다. - 실패, profileUrl이 다름")
    void updateFailedWhenDifferentProfileUrl() {
        User user = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("조앤")
                .bio("백엔드 개발자")
                .profileUrl("profile.io")
                .build();

        User updateUser = User.builder()
                .id(1L)
                .userName("조앤")
                .bio("백엔드 개발자")
                .profileUrl("another.profile.url")
                .build();

        assertThatThrownBy(() -> user.update(updateUser))
                .isInstanceOf(ProfileUpdateNotAllowedException.class);
    }
}
