package botobo.core.config;

import ch.qos.logback.access.servlet.TeeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfiguration {

    @Bean
    public TeeFilter teeFilter() {
        return new TeeFilter();
    }
}
