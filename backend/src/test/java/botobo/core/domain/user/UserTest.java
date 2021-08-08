package botobo.core.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

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
}
