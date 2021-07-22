package botobo.core.documentation;


import botobo.core.auth.application.AuthService;
import botobo.core.auth.dto.LoginRequest;
import botobo.core.auth.dto.TokenResponse;
import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.auth.ui.AuthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그인 문서화 테스트")
@WebMvcTest(AuthController.class)
public class LoginDocumentationTest extends DocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("로그인 - 성공")
    void login() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("githubCode");
        given(authService.createToken(any())).willReturn(
                TokenResponse.of("botobo.access.token")
        );

        // when, then
        document()
                .mockMvc(mockMvc)
                .post("/api/login", loginRequest)
                .build()
                .addStatusAndIdentifier(status().isOk(), "login-success");
    }
}
