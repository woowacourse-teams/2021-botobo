package botobo.core.ui.auth;

import botobo.core.application.AuthService;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/{socialType}")
    public ResponseEntity<TokenResponse> login(@PathVariable String socialType, @RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.createToken(socialType, loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
