package botobo.core.auth.ui;

import botobo.core.exception.UnauthorizedException;
import botobo.core.user.infrastructure.AuthorizationExtractor;
import botobo.core.user.infrastructure.JwtTokenProvider;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String httpMethod = request.getMethod();
        if (httpMethod.equals(HttpMethod.OPTIONS)) {
            return true;
        }
        String accessToken = AuthorizationExtractor.extract(request);
        if (accessToken == null || !jwtTokenProvider.isValidToken(accessToken)) {
            throw new UnauthorizedException("토큰이 유효하지 않습니다.");
        }
        return true;
    }
}