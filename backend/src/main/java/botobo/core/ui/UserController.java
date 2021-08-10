package botobo.core.ui;

import botobo.core.application.UserService;
import botobo.core.domain.user.AppUser;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserNameRequest;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.ui.auth.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findUserOfMine(@AuthenticationPrincipal AppUser appUser) {
        UserResponse userResponse = userService.findById(appUser);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping(value = "/profile")
    public ResponseEntity<ProfileResponse> updateProfileImage(
            @RequestParam(value = "profile", required = false) MultipartFile multipartFile,
            @AuthenticationPrincipal AppUser appUser
    )
            throws IOException {
        ProfileResponse profileResponse = userService.updateProfile(multipartFile, appUser);
        return ResponseEntity.ok(profileResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest,
                                               @AuthenticationPrincipal AppUser appUser) {
        UserResponse userResponse = userService.update(userUpdateRequest, appUser);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/name-check")
    public ResponseEntity<Void> checkDuplicateUserName(@Valid @RequestBody UserNameRequest userNameRequest,
                                                       @AuthenticationPrincipal AppUser appUser) {
        userService.checkDuplicatedUserName(userNameRequest, appUser);
        return ResponseEntity.ok().build();
    }
}
