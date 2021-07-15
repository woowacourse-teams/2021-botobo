package botobo.core.documentation;

import botobo.core.user.application.LoginService;
import botobo.core.user.dto.LoginRequest;
import botobo.core.user.dto.TokenResponse;
import botobo.core.user.ui.LoginController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static botobo.core.documentation.DocumentationUtils.getDocumentRequest;
import static botobo.core.documentation.DocumentationUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그인 문서화 테스트")
@WebMvcTest(LoginController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class LoginDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginService loginService;

    @Test
    @DisplayName("로그인 - 성공")
    void login() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("githubCode");
        given(loginService.createToken(any())).willReturn(
                TokenResponse.of("토큰입니다.")
        );

        // when, then
        mockMvc.perform(post("/login")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(document("login-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }
}
