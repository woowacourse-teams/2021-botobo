package botobo.core.auth;

import botobo.core.AcceptanceTest;
import botobo.core.auth.dto.GithubUserInfoResponse;
import botobo.core.auth.dto.LoginRequest;
import botobo.core.auth.dto.TokenResponse;
import botobo.core.auth.infrastructure.GithubOauthManager;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
                .githubId(1L)
                .profileUrl("github.io")
                .build();

        given(githubOauthManager.getUserInfoFromGithub(any())).willReturn(githubUserInfoResponse);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all()
                .extract();
    }
}
