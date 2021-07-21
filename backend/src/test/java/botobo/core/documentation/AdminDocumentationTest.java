package botobo.core.documentation;

import botobo.core.admin.application.AdminService;
import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminCardResponse;
import botobo.core.admin.dto.AdminWorkbookRequest;
import botobo.core.admin.dto.AdminWorkbookResponse;
import botobo.core.admin.ui.AdminController;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.quiz.exception.CardNotFoundException;
import botobo.core.quiz.exception.CategoryNotFoundException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("관리자용 API 문서화 테스트")
@WebMvcTest(AdminController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class AdminDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("관리자 문제집 생성 - 성공")
    void createCategory() throws Exception {
        // given
        String token = "botobo.access.token";
        AdminWorkbookRequest adminWorkbookRequest = new AdminWorkbookRequest("JAVA");
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);
        given(adminService.createWorkbook(any())).willReturn(
                AdminWorkbookResponse.builder()
                        .id(1L)
                        .name("JAVA")
                        .build()
        );

        // when, then
        mockMvc.perform(post("/admin/workbooks")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminWorkbookRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/admin/workbooks/1"))
                .andDo(document("admin-workbooks-post-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    @Test
    @DisplayName("관리자 카드 생성 - 성공")
    void createCard() throws Exception {
        // given
        String token = "botobo.access.token";
        AdminCardRequest adminCardRequest = new AdminCardRequest("질문1", "답변1", 1L);
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);
        given(adminService.createCard(any())).willReturn(
                AdminCardResponse.builder()
                        .id(1L)
                        .question("질문1")
                        .workbookId(1L)
                        .build()
        );

        // when, then
        mockMvc.perform(post("/admin/cards")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminCardRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/admin/cards/1"))
                .andDo(document("admin-cards-post-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    @Test
    @DisplayName("관리자 카드 생성 - 실패, 문제집 존재하지 않음")
    void createCardWithInvalidCategory() throws Exception {
        // given
        String token = "botobo.access.token";
        AdminCardRequest adminCardRequest = new AdminCardRequest("질문1", "답변1", 1000L);
        given(adminService.createCard(any())).willThrow(new WorkbookNotFoundException());
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);

        // when, then
        mockMvc.perform(post("/admin/cards")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminCardRequest)))
                .andExpect(status().isNotFound())
                .andDo(document("admin-cards-post-fail-invalid-workbook",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }
}
