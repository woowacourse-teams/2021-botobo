package botobo.core.ui.auth;

import botobo.core.application.AuthService;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.infrastructure.auth.JwtRefreshTokenInfo;
import botobo.core.infrastructure.auth.JwtTokenType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "BTOKEN_REFRESH";

    private final AuthService authService;
    private final JwtRefreshTokenInfo jwtRefreshTokenInfo;

    public AuthController(AuthService authService, JwtRefreshTokenInfo jwtRefreshTokenInfo) {
        this.authService = authService;
        this.jwtRefreshTokenInfo = jwtRefreshTokenInfo;
    }

    @PostMapping("/login/{socialType}")
    public ResponseEntity<TokenResponse> login(@PathVariable String socialType, @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.createAccessToken(socialType, loginRequest);
        Long id = authService.extractIdByToken(tokenResponse.getAccessToken(), JwtTokenType.ACCESS_TOKEN);
        Cookie cookie = createRefreshTokenCookie(id);
        response.addCookie(cookie);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> renewToken(@CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken, HttpServletResponse response) {
        authService.validateRefreshToken(refreshToken);
        Long id = authService.extractIdByToken(refreshToken, JwtTokenType.REFRESH_TOKEN);
        TokenResponse tokenResponse = authService.renewAccessToken(id);
        Cookie cookie = createRefreshTokenCookie(id);
        response.addCookie(cookie);
        return ResponseEntity.ok(tokenResponse);
    }

    private Cookie createRefreshTokenCookie(Long id) {
        String refreshToken = authService.createRefreshToken(id);
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtRefreshTokenInfo.getValidityInMilliseconds().intValue());
        return cookie;
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
                                       HttpServletResponse response) {

        authService.removeRefreshToken(refreshToken);
        expireRefreshTokenCookie(response);
        return ResponseEntity.noContent().build();
    }

    private void expireRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
