package botobo.core.acceptance;

import botobo.core.domain.user.SocialType;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.infrastructure.GithubOauthManager;
import botobo.core.ui.auth.OauthManagerFactory;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Auth Acceptance 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    @MockBean
    private GithubOauthManager githubOauthManager;

    @MockBean
    private OauthManagerFactory oauthManagerFactory;

    protected TokenResponse 깃헙_로그인되어_있음() {
        ExtractableResponse<Response> response = 깃헙_로그인_요청();
        return response.as(TokenResponse.class);
    }

    private ExtractableResponse<Response> 깃헙_로그인_요청() {
        LoginRequest loginRequest = new LoginRequest("githubCode", SocialType.GITHUB);
        UserInfoResponse githubUserInfoResponse = GithubUserInfoResponse.builder()
                .userName("githubUser")
                .socialId(2L)
                .profileUrl("github.io")
                .build();

        given(oauthManagerFactory.findOauthMangerBySocialType(SocialType.GITHUB)).willReturn(githubOauthManager);
        given(githubOauthManager.getUserInfo(any())).willReturn(githubUserInfoResponse.toUser());

        return request()
                .post("/api/login", loginRequest)
                .build()
                .extract();
    }

    @Test
    @DisplayName("깃헙 로그인을 한다 - 성공")
    void login() {
        final TokenResponse tokenResponse = 깃헙_로그인되어_있음();
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }
}
