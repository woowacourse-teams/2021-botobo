package botobo.core.ui.auth;

import botobo.core.application.AuthService;
import botobo.core.exception.auth.AccessTokenRenewalException;
import botobo.core.exception.http.UnAuthorizedException;
import botobo.core.infrastructure.auth.AuthorizationExtractor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthorizationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AuthorizationExtractor.extract(request);
        if (isValidAccessToken(accessToken)) {
            return true;
        }
        String refreshToken = extractRefreshToken(request);
        authService.validateRefreshToken(refreshToken);
        throw new AccessTokenRenewalException();
    }

    private boolean isValidAccessToken(String accessToken) {
        try {
            authService.validateAccessToken(accessToken);
            return true;
        } catch (UnAuthorizedException e) {
            return false;
        }
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "BTOKEN_REFRESH");
        if (Objects.isNull(cookie)) {
            return null;
        }
        return cookie.getValue();
    }
}
