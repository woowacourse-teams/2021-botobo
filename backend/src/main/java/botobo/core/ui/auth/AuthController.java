package botobo.core.ui.auth;

import botobo.core.application.AuthService;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.SsrTokenResponse;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.infrastructure.auth.JwtRefreshTokenInfo;
import botobo.core.infrastructure.auth.JwtTokenType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    private static final String SET_COOKIE = "Set-Cookie";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "BTOKEN_REFRESH";

    private final AuthService authService;
    private final JwtRefreshTokenInfo jwtRefreshTokenInfo;

    public AuthController(AuthService authService, JwtRefreshTokenInfo jwtRefreshTokenInfo) {
        this.authService = authService;
        this.jwtRefreshTokenInfo = jwtRefreshTokenInfo;
    }

    @PostMapping("/login/{socialType}")
    public ResponseEntity<TokenResponse> login(@PathVariable String socialType, @RequestBody LoginRequest loginRequest,
                                               HttpServletResponse response) {

        TokenResponse tokenResponse = authService.createAccessToken(socialType, loginRequest);
        Long id = authService.extractIdByToken(tokenResponse.getAccessToken(), JwtTokenType.ACCESS_TOKEN);
        ResponseCookie responseCookie = createRefreshTokenCookie(id);
        response.addHeader(SET_COOKIE, responseCookie.toString());
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> renewToken(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response) {

        authService.validateRefreshToken(refreshToken);
        Long id = authService.extractIdByToken(refreshToken, JwtTokenType.REFRESH_TOKEN);
        TokenResponse tokenResponse = authService.renewAccessToken(id);
        ResponseCookie responseCookie = createRefreshTokenCookie(id);
        response.addHeader(SET_COOKIE, responseCookie.toString());
        return ResponseEntity.ok(tokenResponse);
    }

    private ResponseCookie createRefreshTokenCookie(Long id) {
        String refreshToken = authService.createRefreshToken(id);
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .sameSite("Lax")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtRefreshTokenInfo.getValidityInSeconds().intValue())
                .build();
    }

    @GetMapping("/token/ssr")
    public ResponseEntity<SsrTokenResponse> renewTokenForSsr(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken) {

        authService.validateRefreshToken(refreshToken);
        Long id = authService.extractIdByToken(refreshToken, JwtTokenType.REFRESH_TOKEN);
        String accessToken = authService.renewAccessToken(id).getAccessToken();
        String refreshTokenCookieInfo = createRefreshTokenCookie(id).toString();
        return ResponseEntity.ok(
                SsrTokenResponse.of(
                        accessToken,
                        refreshTokenCookieInfo
                )
        );
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response) {

        authService.removeRefreshToken(refreshToken);
        expireRefreshTokenCookie(response);
        return ResponseEntity.noContent().build();
    }

    private void expireRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .sameSite("Lax")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(SET_COOKIE, responseCookie.toString());
    }
}
