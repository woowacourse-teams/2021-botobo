package botobo.core.documentation;

import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.infrastructure.auth.JwtTokenType;
import botobo.core.ui.auth.AuthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그인 문서화 테스트")
@WebMvcTest(AuthController.class)
class LoginDocumentationTest extends DocumentationTest {

    @Test
    @DisplayName("소셜 로그인 - 성공")
    void login() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("authCode");
        String refreshToken = "botobo.refresh.token";
        Long id = authenticatedUser().getId();

        given(authService.extractIdByToken(authenticatedToken(), JwtTokenType.ACCESS_TOKEN)).willReturn(id);
        given(authService.createAccessToken(anyString(), any(LoginRequest.class))).willReturn(
                TokenResponse.of(authenticatedToken())
        );
        given(authService.createRefreshToken(id)).willReturn(refreshToken);
        given(jwtRefreshTokenInfo.getValidityInSeconds()).willReturn(1000L);

        // when, then
        MvcResult mvcResult = document()
                .mockMvc(mockMvc)
                .post("/login/github", loginRequest)
                .build()
                .status(status().isOk())
                .identifier("login-success");

        MockHttpServletResponse response = mvcResult.getResponse();
        Cookie cookie = response.getCookie("BTOKEN_REFRESH");
        assertThat(cookie.getValue()).isEqualTo(refreshToken);
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getSecure()).isTrue();
        assertThat(cookie.getPath()).isEqualTo("/");
        assertThat(cookie.getMaxAge()).isPositive();
    }

    @Test
    @DisplayName("토큰 재발급 - 성공, 리프레시 토큰을 이용하여 액세스 토큰 재발급")
    void renewToken() throws Exception {
        //given
        String refreshToken = "botobo.refresh.token";
        String renewedAccessToken = "new.access.token";
        String renewedRefreshToken = "new.refresh.token";
        Long id = authenticatedUser().getId();
        given(authService.extractIdByToken(refreshToken, JwtTokenType.REFRESH_TOKEN)).willReturn(id);
        given(authService.renewAccessToken(id)).willReturn(
                TokenResponse.of(renewedAccessToken)
        );
        given(authService.createRefreshToken(id)).willReturn(renewedRefreshToken);
        given(jwtRefreshTokenInfo.getValidityInSeconds()).willReturn(1000L);

        // when, then
        MvcResult mvcResult = document()
                .mockMvc(mockMvc)
                .get("/token")
                .cookie(new Cookie("BTOKEN_REFRESH", refreshToken))
                .build()
                .status(status().isOk())
                .identifier("token-get-success");

        MockHttpServletResponse response = mvcResult.getResponse();
        Cookie cookie = response.getCookie("BTOKEN_REFRESH");
        assertThat(cookie.getValue()).isEqualTo(renewedRefreshToken);
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getSecure()).isTrue();
        assertThat(cookie.getPath()).isEqualTo("/");
        assertThat(cookie.getMaxAge()).isPositive();
    }

    @Test
    @DisplayName("리프레시 토큰을 이용하여 액세스 토큰 재발급 - 성공")
    void renewTokenForSsr() throws Exception {
        //given
        String refreshToken = "botobo.refresh.token";
        String renewedAccessToken = "new.access.token";
        String renewedRefreshToken = "new.refresh.token";
        Long id = authenticatedUser().getId();
        given(authService.extractIdByToken(refreshToken, JwtTokenType.REFRESH_TOKEN)).willReturn(id);
        given(authService.renewAccessToken(id)).willReturn(
                TokenResponse.of(renewedAccessToken)
        );
        given(authService.createRefreshToken(id)).willReturn(renewedRefreshToken);
        given(jwtRefreshTokenInfo.getValidityInSeconds()).willReturn(1000L);

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/token/ssr")
                .cookie(new Cookie("BTOKEN_REFRESH", refreshToken))
                .build()
                .status(status().isOk())
                .identifier("ssr-token-get-success");
    }

    @Test
    @DisplayName("로그아웃 - 성공")
    void logout() throws Exception {
        //given
        String refreshToken = "botobo.refresh.token";

        // when, then
        MvcResult mvcResult = document()
                .mockMvc(mockMvc)
                .get("/logout")
                .auth(authenticatedToken())
                .cookie(new Cookie("BTOKEN_REFRESH", refreshToken))
                .build()
                .status(status().isNoContent())
                .identifier("logout-get-success");

        MockHttpServletResponse response = mvcResult.getResponse();
        Cookie cookie = response.getCookie("BTOKEN_REFRESH");
        assertThat(cookie.getValue()).isBlank();
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getSecure()).isTrue();
        assertThat(cookie.getPath()).isEqualTo("/");
        assertThat(cookie.getMaxAge()).isZero();
    }
}
