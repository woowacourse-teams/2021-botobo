package botobo.core.acceptance;

import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.infrastructure.GithubOauthManager;
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

    protected TokenResponse 로그인되어_있음() {
        ExtractableResponse<Response> response = 로그인_요청();
        return response.as(TokenResponse.class);
    }

    private ExtractableResponse<Response> 로그인_요청() {
        LoginRequest loginRequest = new LoginRequest("githubCode");
        GithubUserInfoResponse githubUserInfoResponse = GithubUserInfoResponse.builder()
                .userName("githubUser")
                .githubId(2L)
                .profileUrl("github.io")
                .build();

        given(githubOauthManager.getUserInfoFromGithub(any())).willReturn(githubUserInfoResponse);

        return request()
                .post("/api/login", loginRequest)
                .build()
                .extract();
    }

    @Test
    @DisplayName("로그인을 한다 - 성공")
    void login() {
        final TokenResponse tokenResponse = 로그인되어_있음();
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }
}
