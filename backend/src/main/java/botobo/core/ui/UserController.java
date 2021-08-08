package botobo.core.ui;

import botobo.core.application.UserService;
import botobo.core.domain.user.AppUser;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.ui.auth.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findUserOfMine(@AuthenticationPrincipal AppUser appUser) {
        UserResponse userResponse = userService.findById(appUser.getId());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/me/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UserUpdateRequest userUpdateRequest,
                                               @AuthenticationPrincipal AppUser appUser) {
        UserResponse userResponse = userService.update(id, userUpdateRequest, appUser);
        return ResponseEntity.ok(userResponse);
    }
}
