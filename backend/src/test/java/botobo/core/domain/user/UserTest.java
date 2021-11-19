package botobo.core.domain.user;

import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.user.ProfileUpdateNotAllowedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

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
        // given
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
                .profileUrl("profile.io")
                .build();

        // when, then
        assertThat(user.getBio()).isEmpty();
    }

    @Test
    @DisplayName("문제집을 포함한 User 객체 생성 - 성공")
    void createWithWorkbook() {
        // given
        Workbook workbook = Workbook.builder()
                .name("workbook")
                .build();

        // when, then
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
        // givne
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
                .profileUrl("profile.io")
                .bio("백엔드 개발자 유저입니다.")
                .build();

        // when, then
        assertThat(user.getBio()).isEqualTo("백엔드 개발자 유저입니다.");
    }

    @Test
    @DisplayName("다른 유저와 이름 비교 - 성공")
    void isSameName() {
        // given
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

        // when, then
        assertThat(user.isSameName(anotherUser)).isTrue();
        assertThat(user.isSameName(pk)).isFalse();
    }

    @Test
    @DisplayName("유저의 정보를 업데이트 한다. - 성공, profileUrl 제외 업데이트")
    void update() {
        // given
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

        // when
        user.update(updateUser);

        // then
        assertAll(
                () -> assertThat(user.getUserName()).isEqualTo("카일"),
                () -> assertThat(user.getBio()).isEqualTo("프론트엔드 개발자"),
                () -> assertThat(user.getProfileUrl()).isEqualTo("profile.io")
        );
    }

    @Test
    @DisplayName("유저의 정보를 업데이트 한다. - 실패, profileUrl이 다름")
    void updateFailedWhenDifferentProfileUrl() {
        // given
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

        // when, then
        assertThatThrownBy(() -> user.update(updateUser))
                .isInstanceOf(ProfileUpdateNotAllowedException.class);
    }
}
