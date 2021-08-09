package botobo.core.acceptance.user;

import botobo.core.acceptance.AuthAcceptanceTest;
import botobo.core.acceptance.DomainAcceptanceTest;
import botobo.core.acceptance.utils.RequestBuilder.HttpResponse;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends DomainAcceptanceTest {

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 성공")
    void findByUserOfMine() {
        // given
        UserInfoResponse userInfoResponse = GithubUserInfoResponse.builder()
                .userName("socialUser")
                .socialId("2")
                .profileUrl("social.io")
                .build();

        //when
        final HttpResponse response = request()
                .get("/api/users/me")
                .auth(소셜_로그인되어_있음(userInfoResponse, SocialType.GITHUB))
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        //then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("socialUser");
        assertThat(userResponse.getProfileUrl()).isEqualTo("social.io");
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
}
