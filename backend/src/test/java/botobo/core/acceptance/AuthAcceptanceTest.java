package botobo.core.acceptance;

import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.exception.ErrorResponse;
import botobo.core.infrastructure.GithubOauthManager;
import botobo.core.infrastructure.OauthManagerFactory;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Auth Acceptance 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    @MockBean
    private GithubOauthManager githubOauthManager;

    @MockBean
    private OauthManagerFactory oauthManagerFactory;

    protected TokenResponse 소셜_로그인되어_있음(SocialType socialType) {
        ExtractableResponse<Response> response = 소셜_로그인_요청(socialType);
        return response.as(TokenResponse.class);
    }

    private ExtractableResponse<Response> 소셜_로그인_요청(SocialType socialType) {
        LoginRequest loginRequest = new LoginRequest("code");
        UserInfoResponse githubUserInfoResponse = GithubUserInfoResponse.builder()
                .userName("socialUser")
                .socialId("2")
                .profileUrl("social.io")
                .build();

        given(oauthManagerFactory.findOauthMangerBySocialType(socialType)).willReturn(githubOauthManager);
        given(githubOauthManager.getUserInfo(any())).willReturn(githubUserInfoResponse.toUser());

        return request()
                .post("/api/login/{socialType}", loginRequest, socialType.name().toLowerCase())
                .build()
                .extract();
    }

    @Test
    @DisplayName("깃헙 로그인을 한다 - 성공")
    void loginWithGithub() {
        // given, when
        final TokenResponse tokenResponse = 소셜_로그인되어_있음(SocialType.GITHUB);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("구글 로그인을 한다 - 성공")
    void loginWithGoogle() {
        // given, when
        final TokenResponse tokenResponse = 소셜_로그인되어_있음(SocialType.GOOGLE);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
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
        assertThat(errorResponse.getMessage()).isEqualTo("존재하지 않는 Social Type 입니다.");
    }

}
