package botobo.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BotoboApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotoboApplication.class, args);
    }

}
