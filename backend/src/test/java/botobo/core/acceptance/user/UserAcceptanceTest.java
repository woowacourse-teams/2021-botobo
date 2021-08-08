package botobo.core.acceptance.user;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends DomainAcceptanceTest {

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 성공")
    void findByUserOfMine() {
        //when
        final HttpResponse response = request()
                .get("/api/users/me")
                .auth(createToken(1L))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        //then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("admin");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
        assertThat(userResponse.getBio()).isEqualTo("");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 실패, 토큰이 없을 경우")
    void findByUserOfMineWhenNotExistToken() {
        //when
        final HttpResponse response = request()
                .get("/api/users/me")
                .build();
        final ErrorResponse errorResponse = response.convertToErrorResponse();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(errorResponse.getMessage()).isEqualTo("토큰이 유효하지 않습니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 성공")
    void update() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new조앤")
                .profileUrl("github.io")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 1L)
                .auth(createToken(1L))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        //then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("new조앤");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
        assertThat(userResponse.getBio()).isEqualTo("new 소개글");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, profileUrl이 다름.")
    void updateFailedDifferentProfileUrl() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new조앤")
                .profileUrl("another.profile.url")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 1L)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("프로필 이미지 수정은 불가합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, id와 AppUser의 id가 다름.")
    void updateFailedDifferentId() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new조앤")
                .profileUrl("github.io")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 2L)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponse.getMessage()).isEqualTo("식별값이 다르므로 내 정보를 수정할 권한이 없습니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, userName은 null이 될 수 없다.")
    void updateFailedUserNameIsNull() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName(null)
                .profileUrl("github.io")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 1L)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원 정보를 수정하기 위해서는 이름이 필요합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, userName은 최소 한 글자 이상")
    void updateFailedEmptyUserName() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("")
                .profileUrl("github.io")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 1L)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 최소 1자 이상, 최대 20자까지 입력 가능합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, userName은 최대 20자")
    void updateFailedLongUserName() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName(stringGenerator(21))
                .profileUrl("github.io")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 1L)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("이름은 최소 1자 이상, 최대 20자까지 입력 가능합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 프로필 사진 필드는 null이 될 수 없다.")
    void updateFailedProfileUrlIsNull() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new 조앤")
                .profileUrl(null)
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 1L)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원 정보를 수정하기 위해서는 프로필 사진이 필요합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 프로필 사진 필드는 ''이 될 수 없다.")
    void updateFailedProfileUrlIsEmpty() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new 조앤")
                .profileUrl("")
                .bio("new 소개글")
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 1L)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원 정보를 수정하기 위해서는 프로필 사진이 필요합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 소개글은 null이 될 수 없다.")
    void updateFailedBioIsNull() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new 조앤")
                .profileUrl("github.io")
                .bio(null)
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 1L)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("회원 정보를 수정하기 위해서는 소개글은 최소 0자 이상이 필요합니다.");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 수정한다. - 실패, 소개글은 null이 될 수 없다.")
    void updateFailedLongBio() {
        //given
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("new 조앤")
                .profileUrl("github.io")
                .bio(stringGenerator(256))
                .build();
        //when
        final HttpResponse response = request()
                .put("/api/users/me/{id}", userUpdateRequest, 1L)
                .auth(createToken(1L))
                .build();

        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.getMessage()).isEqualTo("소개글은 최대 255자까지 가능합니다.");
    }
}
