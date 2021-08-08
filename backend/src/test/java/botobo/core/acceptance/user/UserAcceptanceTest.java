package botobo.core.acceptance.user;

import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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
}
