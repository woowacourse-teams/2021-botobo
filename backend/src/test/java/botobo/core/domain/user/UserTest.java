package botobo.core.domain.user;

import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.user.ProfileUpdateNotAllowedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

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
                .socialId("1")
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
                .socialId("1")
                .userName("user")
                .profileUrl("profile.io")
                .build();
        assertThat(user.getBio()).isEmpty();
    }

    @Test
    @DisplayName("문제집을 포함한 User 객체 생성 - 성공")
    void createWithWorkbook() {
        // given
        Workbook workbook = Workbook.builder()
                .name("workbook")
                .build();
        assertThatCode(() -> User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
                .profileUrl("profile.io")
                .workbooks(Collections.singletonList(workbook))
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Bio를 포함한 User 객체 생성 - 성공")
    void createUserWithBio() {
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
                .profileUrl("profile.io")
                .bio("백엔드 개발자 유저입니다.")
                .build();
        assertThat(user.getBio()).isEqualTo("백엔드 개발자 유저입니다.");
    }

    @Test
    @DisplayName("다른 유저와 이름 비교 - 성공")
    void isSameName() {
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
                .profileUrl("profile.io")
                .bio("백엔드 개발자 유저입니다.")
                .build();
        User anotherUser = User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
                .profileUrl("profile.io")
                .bio("백엔드 개발자 유저입니다.")
                .build();
        User pk = User.builder()
                .id(1L)
                .socialId("1")
                .userName("pk")
                .profileUrl("profile.io")
                .bio("백엔드 개발자 유저입니다.")
                .build();
        assertThat(user.isSameName(anotherUser)).isTrue();
        assertThat(user.isSameName(pk)).isFalse();
    }

    @Test
    @DisplayName("유저의 정보를 업데이트 한다. - 성공, profileUrl 제외 업데이트")
    void update() {
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
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
                .socialId("1")
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
