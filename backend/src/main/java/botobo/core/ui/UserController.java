package botobo.core.ui;

import botobo.core.application.UserService;
import botobo.core.domain.user.AppUser;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.ui.auth.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/me")
    public ResponseEntity<ProfileResponse> updateProfileImage(@AuthenticationPrincipal AppUser appUser,
                                                              @RequestParam("image")MultipartFile multipartFile) {
        // TODO S3 bucket에 연결하기
        return ResponseEntity.ok(ProfileResponse.builder().build());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal AppUser appUser,
                                                      @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateProfile(userUpdateRequest, appUser);
        return ResponseEntity.ok(UserResponse.builder().build());
    }
}
