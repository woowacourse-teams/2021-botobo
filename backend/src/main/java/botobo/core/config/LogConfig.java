package botobo.core.config;

import ch.qos.logback.access.servlet.TeeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class LogConfig implements WebMvcConfigurer {
    private final Environment environment;

    public LogConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public TeeFilter teeFilter() {
        return new TeeFilter();
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (isLocal() || isTest()) {
            for (HttpMessageConverter<?> converter : converters) {
                if (converter instanceof MappingJackson2HttpMessageConverter) {
                    MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) converter;
                    jacksonConverter.setPrettyPrint(true);
                }
            }
        }
    }

    private boolean isLocal() {
        return Arrays.asList(environment.getActiveProfiles()).contains("local");
    }

    private boolean isTest() {
        return Arrays.asList(environment.getActiveProfiles()).contains("test");
    }
}
