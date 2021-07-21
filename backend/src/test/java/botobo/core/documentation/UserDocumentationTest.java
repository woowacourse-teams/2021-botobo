package botobo.core.documentation;

import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.user.application.UserService;
import botobo.core.user.dto.UserResponse;
import botobo.core.user.ui.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import static botobo.core.documentation.DocumentationUtils.getDocumentRequest;
import static botobo.core.documentation.DocumentationUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("유저용 API 문서화 테스트")
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class UserDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(document("users-find-me-get-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }
}
