package botobo.core.ui.auth;

import botobo.core.application.AuthService;
import botobo.core.exception.auth.AccessTokenRenewalException;
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
        // 올바른 액세스 토큰이면 요청한 api를 실행한다.
        if (authService.isValidAccessToken(accessToken)) {
            return true;
        }

        // 올바르지 않은 access token인 경우 쿠키에서 refresh 토큰을 얻어낸다. 만약 쿠키가 없다면 null을 반환한다
        String refreshToken = extractRefreshToken(request);

        // 다음 세 가지 상황에 예외를 던진다 : 다시 로그인 해야한다
        // 1. refresh token 이 없는 경우 (null인 경우) : TokenNotValidException
        // 2. refresh token 이 만료된 경우 : TokenExpiredException
        // 3. refresh token 이 redis에 저장된 것이 아닌 경우 (가장 최근에 발급한 것이 아닌 경우) : TokenNotValidExcepiton
        authService.validateRefreshToken(refreshToken);

        // 리프레시 토큰이 올바른 경우 (액세스 토큰이 정상적으로 만료된 경우)
        // 클라이언트는 /api/token 로 요청하여 access token, refresh token을 둘 다 재발급 받는다.
        // 재발급 받은 후, 클라이언트는 해당 토큰들을 이용하여 기존에 보냈던 요청을 다시 보내야한다 (frontend 요구사항)
        throw new AccessTokenRenewalException();
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "BTOKEN_REFRESH");
        if (Objects.isNull(cookie)) {
            return null;
        }
        return cookie.getValue();
    }
}
