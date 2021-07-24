package botobo.core.documentation;

import botobo.core.admin.application.AdminService;
import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminCardResponse;
import botobo.core.admin.dto.AdminWorkbookRequest;
import botobo.core.admin.dto.AdminWorkbookResponse;
import botobo.core.admin.ui.AdminController;
import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("관리자용 API 문서화 테스트")
@WebMvcTest(AdminController.class)
public class AdminDocumentationTest extends DocumentationTest {

    @Autowired
    private MockMvc mockMvc;

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

        document()
                .mockMvc(mockMvc)
                .post("/api/admin/workbooks", adminWorkbookRequest)
                .auth(token)
                .locationHeader("/api/admin/workbooks/1")
                .build()
                .status(status().isCreated())
                .identifier("admin-workbooks-post-success");
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
        document()
                .mockMvc(mockMvc)
                .post("/api/admin/cards", adminCardRequest)
                .auth(token)
                .locationHeader("/api/admin/cards/1")
                .build()
                .status(status().isCreated())
                .identifier("admin-cards-post-success");
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
        document()
                .mockMvc(mockMvc)
                .post("/api/admin/cards", adminCardRequest)
                .auth(token)
                .build()
                .status(status().isNotFound())
                .identifier("admin-cards-post-fail-invalid-workbook");

    }
}
