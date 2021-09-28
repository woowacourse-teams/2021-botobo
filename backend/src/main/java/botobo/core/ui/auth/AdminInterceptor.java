package botobo.core.ui.auth;

import botobo.core.application.AuthService;
import botobo.core.infrastructure.auth.AuthorizationExtractor;
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
        String credentials = AuthorizationExtractor.extract(request);
        authService.validateAdmin(credentials);
        return true;
    }
}
