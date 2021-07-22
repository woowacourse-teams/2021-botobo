package botobo.core.user;

import botobo.core.AcceptanceTest;
import botobo.core.auth.AuthAcceptanceTest;
import botobo.core.auth.infrastructure.GithubOauthManager;
import botobo.core.common.exception.ErrorResponse;
import botobo.core.user.dto.UserResponse;
import botobo.core.utils.RequestBuilder.HttpResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AuthAcceptanceTest {

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 성공")
    void findByUserOfMine() {
        //when
        final HttpResponse response = request()
                .get("/api/users/me")
                .auth(로그인되어_있음().getAccessToken())
                .build();

        UserResponse userResponse = response.convertBody(UserResponse.class);

        //then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo("githubUser");
        assertThat(userResponse.getProfileUrl()).isEqualTo("github.io");
    }

    @Test
    @DisplayName("로그인 한 유저의 정보를 가져온다. - 실패, 토큰이 없을 경우")
    void findByUserOfMineWhenNotExistToken() {
        //when
        final HttpResponse response = request()
                .get("/api/users/me")
                .build();
        final ErrorResponse errorResponse = response.errorResponse();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(errorResponse.getMessage()).isEqualTo("토큰 추출에 실패했습니다.");
    }
}
