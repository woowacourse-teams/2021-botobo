package botobo.core.config;

import botobo.core.application.AuthService;
import botobo.core.ui.auth.AdminInterceptor;
import botobo.core.ui.auth.AuthenticationPrincipalArgumentResolver;
import botobo.core.ui.auth.AuthorizationInterceptor;
import botobo.core.ui.auth.PathMatcherInterceptor;
import botobo.core.ui.auth.PathMethod;
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
        registry.addInterceptor(authPathMatcherInterceptor());
        registry.addInterceptor(adminPathMatcherInterceptor());
    }

    @Bean
    public PathMatcherInterceptor authPathMatcherInterceptor() {
        return new PathMatcherInterceptor(authorizationInterceptor())
                .addPathPatterns("/api/**", PathMethod.ANY)
                .excludePathPatterns("/api/**", PathMethod.OPTIONS)
                .excludePathPatterns("/api/workbooks", PathMethod.GET)
                .excludePathPatterns("/api/quizzes/guest", PathMethod.GET)
                .excludePathPatterns("/api/login/**", PathMethod.POST)
                .excludePathPatterns("/api/docs/**", PathMethod.GET)
                .excludePathPatterns("/api/workbooks/public", PathMethod.GET)
                .excludePathPatterns("/api/workbooks/public/**", PathMethod.GET)
                .excludePathPatterns("/api/search/**", PathMethod.GET);
    }

    @Bean
    public PathMatcherInterceptor adminPathMatcherInterceptor() {
        return new PathMatcherInterceptor(adminInterceptor())
                .addPathPatterns("/api/admin/workbooks", PathMethod.POST)
                .addPathPatterns("/api/admin/cards", PathMethod.POST)
                .excludePathPatterns("/api/**", PathMethod.OPTIONS);
    }
}
