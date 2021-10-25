package botobo.core.acceptance;

import botobo.core.acceptance.utils.RequestBuilder;
import botobo.core.domain.user.SocialType;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.SsrTokenResponse;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.exception.common.ErrorResponse;
import botobo.core.infrastructure.auth.JwtTokenType;
import io.restassured.http.Cookie;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static botobo.core.utils.Fixture.oz;
import static botobo.core.utils.Fixture.pk;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("Auth Acceptance 테스트")
class AuthAcceptanceTest extends DomainAcceptanceTest {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "BTOKEN_REFRESH";
    private static final String COOKIE_DOMAIN_VALUE = ".test.botobo.kr";

    @Test
    @DisplayName("깃헙 로그인을 한다 - 성공")
    void loginWithGithub() {
        // given, when
        final String accessToken = 소셜_로그인되어_있음(pk, SocialType.GITHUB);

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
        // given, when
        final String accessToken = 소셜_로그인되어_있음(oz, SocialType.GOOGLE);

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
                .post("/login/{socialType}", loginRequest, "kakao")
                .build();

        // then
        ErrorResponse errorResponse = response.convertBody(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse.getMessage()).isEqualTo("존재하지 않는 소셜 로그인 방식입니다.");
    }

    @Test
    @DisplayName("로그인을 한다 - 성공, 액세스 토큰은 응답 body에 리프레시 토큰은 쿠키에 포함한다.")
    void loginSuccessWithNewTokens() {
        // when
        ExtractableResponse<Response> response = 소셜_로그인_요청(pk, SocialType.GITHUB);

        // then
        String accessToken = response.as(TokenResponse.class).getAccessToken();
        Cookie cookie = response.detailedCookie(REFRESH_TOKEN_COOKIE_NAME);
        String refreshToken = cookie.getValue();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(accessToken).isNotEmpty();
        assertThat(refreshToken).isNotEmpty();
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.isSecured()).isTrue();
        assertThat(cookie.getMaxAge()).isPositive();
        assertThat(cookie.getDomain()).isEqualTo(COOKIE_DOMAIN_VALUE);
    }

    @Test
    @DisplayName("토큰을 재발급 받는다 - 성공, 유효한 리프레시 토큰이 쿠키에 있는 경우")
    void renewToken() {
        // given
        ExtractableResponse<Response> response = 소셜_로그인_요청(pk, SocialType.GITHUB);
        String refreshToken = response.cookies().get(REFRESH_TOKEN_COOKIE_NAME);

        // when
        RequestBuilder.HttpResponse httpResponse = request()
                .get("/token")
                .cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .build();

        // then
        String newAccessToken = httpResponse.convertBody(TokenResponse.class).getAccessToken();
        Cookie cookie = httpResponse.detailedCookie(REFRESH_TOKEN_COOKIE_NAME);
        String newRefreshToken = cookie.getValue();

        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newAccessToken).isNotEmpty();
        assertThat(newRefreshToken).isNotEmpty();
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.isSecured()).isTrue();
        assertThat(cookie.getMaxAge()).isPositive();
        assertThat(cookie.getDomain()).isEqualTo(COOKIE_DOMAIN_VALUE);
    }

    @Test
    @DisplayName("SSR을 위한 서버를 위해 토큰을 재발급 받는다 - 성공, 유효한 리프레시 토큰이 쿠키에 있는 경우")
    void renewTokenForSsr() {
        // given
        ExtractableResponse<Response> response = 소셜_로그인_요청(pk, SocialType.GITHUB);
        String refreshToken = response.cookies().get(REFRESH_TOKEN_COOKIE_NAME);

        // when
        RequestBuilder.HttpResponse httpResponse = request()
                .get("/token/ssr")
                .cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .build();

        // then
        SsrTokenResponse ssrTokenResponse = httpResponse.convertBody(SsrTokenResponse.class);
        String newAccessToken = ssrTokenResponse.getAccessToken();
        String refreshTokenCookieInfo = ssrTokenResponse.getRefreshTokenCookieInfo();

        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newAccessToken).isNotEmpty();
        assertThat(refreshTokenCookieInfo).isNotEmpty();
    }

    @Test
    @DisplayName("토큰을 재발급 받는다 - 실패, 쿠키에 리프레시 토큰이 없는 경우")
    void renewTokenWhenHasNotRefreshToken() {
        // when
        RequestBuilder.HttpResponse httpResponse = request()
                .get("/token")
                .build();

        // then
        ErrorResponse errorResponse = httpResponse.convertToErrorResponse();
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(errorResponse.getCode()).isEqualTo("A001");
        assertThat(errorResponse.getMessage()).contains("토큰이 유효하지 않습니다");
    }

    @Test
    @DisplayName("토큰을 재발급 받는다 - 실패, 유효하지 않은 리프레시 토큰의 경우")
    void renewTokenWhenHasInvalidRefreshToken() {
        // given
        ExtractableResponse<Response> response = 소셜_로그인_요청(pk, SocialType.GITHUB);
        String invalidRefreshToken = "invalidRefreshToken";

        // when
        RequestBuilder.HttpResponse httpResponse = request()
                .get("/token")
                .cookie(REFRESH_TOKEN_COOKIE_NAME, invalidRefreshToken)
                .build();

        // then
        ErrorResponse errorResponse = httpResponse.convertToErrorResponse();
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(errorResponse.getCode()).isEqualTo("A001");
        assertThat(errorResponse.getMessage()).contains("토큰이 유효하지 않습니다");
    }

    @Test
    @DisplayName("토큰을 재발급 받는다 - 실패, 만료된 리프레시 토큰의 경우")
    void renewTokenWhenHasExpiredRefreshToken() {
        // given
        ExtractableResponse<Response> response = 소셜_로그인_요청(pk, SocialType.GITHUB);
        String refreshToken = response.cookies().get(REFRESH_TOKEN_COOKIE_NAME);

        Long id = jwtTokenProvider.getIdFromPayLoad(refreshToken, JwtTokenType.REFRESH_TOKEN);
        String expiredRefreshToken = jwtTokenProvider.createRefreshToken(id, -1000L);

        // when
        RequestBuilder.HttpResponse httpResponse = request()
                .get("/token")
                .cookie(REFRESH_TOKEN_COOKIE_NAME, expiredRefreshToken)
                .build();

        // then
        ErrorResponse errorResponse = httpResponse.convertToErrorResponse();
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(errorResponse.getCode()).isEqualTo("A002");
        assertThat(errorResponse.getMessage()).contains("만료된 토큰입니다");
    }

    @Test
    @DisplayName("토큰을 재발급 받는다 - 실패, 레디스에 저장된 리프레시 토큰과 다른 경우")
    void renewTokenWhenNotStoredRefreshToken() {
        // given
        ExtractableResponse<Response> response = 소셜_로그인_요청(pk, SocialType.GITHUB);
        String storedRefreshToken = response.cookies().get(REFRESH_TOKEN_COOKIE_NAME);

        Long id = jwtTokenProvider.getIdFromPayLoad(storedRefreshToken, JwtTokenType.REFRESH_TOKEN);
        String notStoredRefreshToken = jwtTokenProvider.createRefreshToken(id, 123456789L);

        // when
        RequestBuilder.HttpResponse httpResponse = request()
                .get("/token")
                .cookie(REFRESH_TOKEN_COOKIE_NAME, notStoredRefreshToken)
                .build();

        // then
        ErrorResponse errorResponse = httpResponse.convertToErrorResponse();
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(errorResponse.getCode()).isEqualTo("A001");
        assertThat(errorResponse.getMessage()).contains("토큰이 유효하지 않습니다");
    }

    @Test
    @DisplayName("로그아웃한다 - 성공, 유효한 토큰인 경우 쿠키를 지우고 레디스의 리프레시 토큰도 지운다.")
    void logoutWithValidRefreshToken() {
        // given
        ExtractableResponse<Response> response = 소셜_로그인_요청(pk, SocialType.GITHUB);
        String refreshToken = response.cookies().get(REFRESH_TOKEN_COOKIE_NAME);

        // when
        RequestBuilder.HttpResponse httpResponse = request()
                .get("/logout")
                .cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .build();

        // then
        Cookie cookie = httpResponse.detailedCookie(REFRESH_TOKEN_COOKIE_NAME);
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(cookie.getValue()).isBlank();
        assertThat(cookie.getMaxAge()).isZero();
    }

    @Test
    @DisplayName("로그아웃한다 - 성공, 저장된 리프레시 토큰이 아닌 경우에 레디스에는 영향을 주지 않는다.")
    void logoutWithNotStoredRefreshToken() {
        // given
        ExtractableResponse<Response> response = 소셜_로그인_요청(pk, SocialType.GITHUB);
        String storedRefreshToken = response.cookies().get(REFRESH_TOKEN_COOKIE_NAME);

        Long id = jwtTokenProvider.getIdFromPayLoad(storedRefreshToken, JwtTokenType.REFRESH_TOKEN);
        String notStoredRefreshToken = jwtTokenProvider.createRefreshToken(id, 2000000L);

        // when
        RequestBuilder.HttpResponse httpResponse = request()
                .get("/logout")
                .cookie(REFRESH_TOKEN_COOKIE_NAME, notStoredRefreshToken)
                .build();

        // then
        Cookie cookie = httpResponse.detailedCookie(REFRESH_TOKEN_COOKIE_NAME);
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(cookie.getValue()).isBlank();
        assertThat(cookie.getMaxAge()).isZero();
    }
}
