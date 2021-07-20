package botobo.core.auth.ui;

import botobo.core.auth.infrastructure.AuthorizationExtractor;
import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.exception.UnauthorizedException;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AuthorizationInterceptor implements HandlerInterceptor {
    private static final String ORIGIN = "Origin";

    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isPreflightRequest(request)) {
            return true;
        }
        String credentials = AuthorizationExtractor.extract(request);
        if (credentials == null || !jwtTokenProvider.isValidToken(credentials)) {
            throw new UnauthorizedException("토큰이 유효하지 않습니다.");
        }
        return true;
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader(ORIGIN)) && request.getMethod().equals(HttpMethod.OPTIONS);
    }
}