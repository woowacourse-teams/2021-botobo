package botobo.core.auth.ui;

import botobo.core.auth.application.AuthService;
import botobo.core.auth.infrastructure.AuthorizationExtractor;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }
        String credentials = AuthorizationExtractor.extract(request);
        authService.validateAdmin(credentials);
        return true;
    }
}
