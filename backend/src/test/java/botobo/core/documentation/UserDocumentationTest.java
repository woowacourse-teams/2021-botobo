package botobo.core.documentation;

import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.user.application.UserService;
import botobo.core.user.dto.UserResponse;
import botobo.core.user.ui.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("유저용 API 문서화 테스트")
@WebMvcTest(UserController.class)
public class UserDocumentationTest extends DocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("현재 로그인 한 유저 조회 - 성공")
    void findUserOfMine() throws Exception {
        String token = "botobo.access.token";
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build();

        given(jwtTokenProvider.isValidToken(token)).willReturn(true);
        given(userService.findById(anyLong())).willReturn(userResponse);

        document()
                .mockMvc(mockMvc)
                .get("/api/users/me")
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("users-find-me-get-success");
    }
}
