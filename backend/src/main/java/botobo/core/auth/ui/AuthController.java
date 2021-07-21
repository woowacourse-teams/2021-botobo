package botobo.core.auth.ui;


import botobo.core.auth.application.AuthService;
import botobo.core.auth.dto.LoginRequest;
import botobo.core.auth.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.createToken(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}