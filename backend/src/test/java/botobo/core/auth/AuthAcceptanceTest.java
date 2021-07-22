package botobo.core.auth;

import botobo.core.AcceptanceTest;
import botobo.core.auth.dto.GithubUserInfoResponse;
import botobo.core.auth.dto.LoginRequest;
import botobo.core.auth.dto.TokenResponse;
import botobo.core.auth.infrastructure.GithubOauthManager;
import botobo.core.utils.RequestBuilder.HttpResponse;
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

    @Test
    @DisplayName("로그인을 한다 - 성공")
    void login() {
        // given
        LoginRequest loginRequest = new LoginRequest("githubCode");
        GithubUserInfoResponse githubUserInfoResponse = GithubUserInfoResponse.builder()
                .userName("githubUser")
                .githubId(1L)
                .profileUrl("github.io")
                .build();
        given(githubOauthManager.getUserInfoFromGithub(any())).willReturn(githubUserInfoResponse);

        // when
        final HttpResponse response = request()
                .post("/api/login", loginRequest)
                .build();

        // then
        final TokenResponse tokenResponse = response.convertBody(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }
}
