package botobo.core.ui;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/infra")
public class InfraController {

    private final Environment environment;

    public InfraController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/profile")
    public String profile() {
        return Arrays.stream(environment.getActiveProfiles())
                .findFirst()
                .orElse("nothing");
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
