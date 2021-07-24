package botobo.core.config;

import botobo.core.quiz.ui.WorkbookCriteriaArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new WorkbookCriteriaArgumentResolver());
    }

    @Bean
    public WorkbookCriteriaArgumentResolver workbookCriteriaArgumentResolver() {
        return new WorkbookCriteriaArgumentResolver();
    }
}
