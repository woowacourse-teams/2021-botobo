package botobo.core.config;

import botobo.core.application.AuthService;
import botobo.core.ui.auth.AdminInterceptor;
import botobo.core.ui.auth.AuthenticationPrincipalArgumentResolver;
import botobo.core.ui.auth.AuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

    private final AuthService authService;

    public AuthenticationPrincipalConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor(authService);
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor(authService);
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/quizzes/guest", "/api/docs/**", "/api/workbooks", "/api/workbooks/public/**");

        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/api/admin/**");
    }
}
