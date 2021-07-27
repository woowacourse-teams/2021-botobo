package botobo.core.documentation;


import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.ui.auth.AuthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그인 문서화 테스트")
@WebMvcTest(AuthController.class)
public class LoginDocumentationTest extends DocumentationTest {

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
                .status(status().isOk())
                .identifier("login-success");
    }
}
