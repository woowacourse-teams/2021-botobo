package botobo.core.ui;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/infra")
public class InfraController {

    private final Environment environment;

    public InfraController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/port")
    public String port() {
        return environment.getProperty("local.server.port");
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
