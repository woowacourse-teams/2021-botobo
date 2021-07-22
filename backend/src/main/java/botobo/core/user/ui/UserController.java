package botobo.core.user.ui;

import botobo.core.auth.ui.AuthenticationPrincipal;
import botobo.core.user.application.UserService;
import botobo.core.user.domain.LoginUser;
import botobo.core.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findUserOfMine(@AuthenticationPrincipal LoginUser loginUser) {
        UserResponse userResponse = userService.findById(loginUser.getId());
        return ResponseEntity.ok(userResponse);
    }
}
