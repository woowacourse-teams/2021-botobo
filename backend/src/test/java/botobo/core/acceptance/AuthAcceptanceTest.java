package botobo.core.acceptance;

import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.exception.common.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("Auth Acceptance 테스트")
public class AuthAcceptanceTest extends DomainAcceptanceTest {

    @Test
    @DisplayName("깃헙 로그인을 한다 - 성공")
    void loginWithGithub() {
        // given
        UserInfoResponse userInfoResponse = GithubUserInfoResponse.builder()
                .userName("socialUser")
                .socialId("2")
                .profileUrl("social.io")
                .build();

        // when
        final String accessToken = 소셜_로그인되어_있음(userInfoResponse, SocialType.GITHUB);

        // then
        assertThat(accessToken).isNotNull();
        then(githubOauthManager)
                .should(times(1))
                .getUserInfo(any());
        then(googleOauthManager)
                .should(never())
                .getUserInfo(any());
    }

    @Test
    @DisplayName("구글 로그인을 한다 - 성공")
    void loginWithGoogle() {
        // given
        UserInfoResponse userInfoResponse = GithubUserInfoResponse.builder()
                .userName("socialUser")
                .socialId("2")
                .profileUrl("social.io")
                .build();

        // when
        final String accessToken = 소셜_로그인되어_있음(userInfoResponse, SocialType.GOOGLE);

        // then
        assertThat(accessToken).isNotNull();
        then(githubOauthManager)
                .should(never())
                .getUserInfo(any());
        then(googleOauthManager)
                .should(times(1))
                .getUserInfo(any());
    }

    @Test
    @DisplayName("소셜 로그인을 한다 - 실패, 존재하지 않는 SocialType일 경우")
    void loginWithSocial() {
        // given
        LoginRequest loginRequest = new LoginRequest("code");

        // when
        final RequestBuilder.HttpResponse response = request()
                .post("/api/login/{socialType}", loginRequest, "kakao")
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("존재하지 않는 소셜 로그인 방식입니다.");
    }

}
