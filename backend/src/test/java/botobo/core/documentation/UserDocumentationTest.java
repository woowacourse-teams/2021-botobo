package botobo.core.documentation;

import botobo.core.application.UserService;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.dto.tag.FilterCriteria;
import botobo.core.dto.user.ProfileResponse;
import botobo.core.dto.user.UserFilterResponse;
import botobo.core.dto.user.UserNameRequest;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.ui.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("유저용 API 문서화 테스트")
@WebMvcTest(UserController.class)
class UserDocumentationTest extends DocumentationTest {

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("회원 정보 조회 - 성공")
    void findUserOfMine() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .userName("user")
                .profileUrl("profile.io")
                .bio("user 소개")
                .build();

        given(userService.findById(any(AppUser.class))).willReturn(userResponse);

        document()
                .mockMvc(mockMvc)
                .get("/users/me")
                .auth(authenticatedToken())
                .build()
                .status(status().isOk())
                .identifier("users-find-me-get-success");
    }

    @Test
    @DisplayName("프로필 이미지 수정 - 성공")
    void updateProfile() throws Exception {
        ProfileResponse profileResponse = ProfileResponse.builder()
                .profileUrl("https://cloudfront.com/users/user/aaabbbccc_210807.png")
                .build();

        given(userService.updateProfile(any(), any(AppUser.class))).willReturn(profileResponse);

        document()
                .mockMvc(mockMvc)
                .multipart("/users/profile", "botobo", "profile")
                .auth(authenticatedToken())
                .build()
                .status(status().isOk())
                .identifier("users-update-profile-post-success");
    }

    @Test
    @DisplayName("회원 정보 수정 - 성공")
    void update() throws Exception {
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .userName("수정된_이름")
                .bio("수정된 바이오")
                .profileUrl("profile.io")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .userName("수정된_이름")
                .bio("수정된 바이오")
                .profileUrl("profile.io")
                .build();

        given(userService.update(any(UserUpdateRequest.class), any(AppUser.class))).willReturn(userResponse);

        document()
                .mockMvc(mockMvc)
                .put("/users/me", userUpdateRequest)
                .auth(authenticatedToken())
                .build()
                .status(status().isOk())
                .identifier("users-update-put-success");
    }

    @Test
    @DisplayName("회원명 중복 조회 - 성공")
    void checkSameUserNameAlreadyExist() throws Exception {
        UserNameRequest userNameRequest = UserNameRequest.builder()
                .userName("중복되지_않는_이름")
                .build();

        AppUser appUser = AppUser.builder()
                .id(1L)
                .role(Role.USER)
                .build();

        doNothing()
                .when(userService)
                .checkDuplicatedUserName(userNameRequest, appUser);

        document()
                .mockMvc(mockMvc)
                .post("/users/name-check", userNameRequest)
                .auth(authenticatedToken())
                .build()
                .status(status().isOk())
                .identifier("users-name-check-post-success");
    }

    @DisplayName("문제집명을 포함한 문제집의 유저 조회 - 성공")
    @Test
    void findAllUsersByWorkbookName() throws Exception {
        // given
        final User 조앤 = User.builder()
                .id(1L)
                .userName("조앤")
                .build();
        final User 민정 = User.builder()
                .id(2L)
                .userName("민정")
                .build();
        List<UserFilterResponse> userResponses = UserFilterResponse.listOf(List.of(조앤, 민정));

        given(userService.findAllUsersByWorkbookName(any(FilterCriteria.class)))
                .willReturn(userResponses);

        // when - then
        document()
                .mockMvc(mockMvc)
                .get("/users?workbook=java")
                .build()
                .status(status().isOk())
                .identifier("users-get-success");
    }
}
