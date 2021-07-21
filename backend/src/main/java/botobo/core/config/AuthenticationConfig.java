package botobo.core.config;

import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.auth.ui.AuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/quizzes/guest");
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor(jwtTokenProvider);
    }
}
