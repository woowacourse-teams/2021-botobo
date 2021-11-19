package botobo.core.config;

import botobo.core.application.AuthService;
import botobo.core.ui.auth.AuthenticationPrincipalArgumentResolver;
import botobo.core.ui.auth.AuthorizationInterceptor;
import botobo.core.ui.auth.PathMatcherInterceptor;
import botobo.core.ui.auth.PathMethod;
import botobo.core.ui.auth.PublicAuthorizationInterceptor;
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
    public PublicAuthorizationInterceptor publicAuthorizationInterceptor() {
        return new PublicAuthorizationInterceptor(authService);
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
        registry.addInterceptor(publicAuthPathMatcherInterceptor());
    }

    @Bean
    public PathMatcherInterceptor authPathMatcherInterceptor() {
        return new PathMatcherInterceptor(authorizationInterceptor())
                .addPathPatterns("/**", PathMethod.ANY)
                .excludePathPatterns("/", PathMethod.GET)
                .excludePathPatterns("/index.html", PathMethod.GET)
                .excludePathPatterns("/favicon.ico", PathMethod.GET)
                .excludePathPatterns("/error", PathMethod.GET)
                .excludePathPatterns("/**", PathMethod.OPTIONS)
                .excludePathPatterns("/infra/**", PathMethod.GET)
                .excludePathPatterns("/quizzes/**", PathMethod.GET)
                .excludePathPatterns("/login/**", PathMethod.POST)
                .excludePathPatterns("/token/**", PathMethod.GET)
                .excludePathPatterns("/logout", PathMethod.GET)
                .excludePathPatterns("/workbooks/public", PathMethod.GET)
                .excludePathPatterns("/workbooks/public/**", PathMethod.GET)
                .excludePathPatterns("/workbooks/download", PathMethod.GET)
                .excludePathPatterns("/ranks/**", PathMethod.GET)
                .excludePathPatterns("/tags", PathMethod.GET)
                .excludePathPatterns("/users", PathMethod.GET)
                .excludePathPatterns("/search/**", PathMethod.GET);
    }

    @Bean
    public PathMatcherInterceptor publicAuthPathMatcherInterceptor() {
        return new PathMatcherInterceptor(publicAuthorizationInterceptor())
                .addPathPatterns("/workbooks/public/?*", PathMethod.GET);
    }
}
