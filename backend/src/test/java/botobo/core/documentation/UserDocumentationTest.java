package botobo.core.documentation;

import botobo.core.application.UserService;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserResponse;
import botobo.core.ui.UserController;
import botobo.core.utils.FileFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("유저용 API 문서화 테스트")
@WebMvcTest(UserController.class)
public class UserDocumentationTest extends DocumentationTest {

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("현재 로그인 한 유저 조회 - 성공")
    void findUserOfMine() throws Exception {
        String token = "botobo.access.token";
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build();
        AppUser appUser = AppUser.builder()
                .id(1L)
                .role(Role.USER)
                .build();

        given(authService.findAppUserByToken(token)).willReturn(appUser);
        given(userService.findById(anyLong())).willReturn(userResponse);

        document()
                .mockMvc(mockMvc)
                .get("/api/users/me")
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("users-find-me-get-success");
    }

    @Test
    @DisplayName("유저의 프로필 이미지 수정 - 성공")
    void updateProfile() throws Exception {
        String token = "botobo.access.token";
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build();
        AppUser appUser = AppUser.builder()
                .id(1L)
                .role(Role.USER)
                .build();
        MockMultipartFile mockMultipartFile = FileFactory.testFile("png");

        ProfileResponse profileResponse = ProfileResponse.builder()
                .profileUrl("https://cloudfront.com/users/user/aaabbbccc_210807.png")
                .build();

        given(authService.findAppUserByToken(token)).willReturn(appUser);
        given(userService.updateProfile(mockMultipartFile, appUser)).willReturn(profileResponse);


        document()
                .mockMvc(mockMvc)
                .multipart("/api/users/profile", "botobo", "profile")
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("users-update-profile-get-success");
    }
}
