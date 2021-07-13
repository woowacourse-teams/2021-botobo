package botobo.core.documentation;

import botobo.core.admin.application.AdminService;
import botobo.core.admin.dto.AdminAnswerRequest;
import botobo.core.admin.dto.AdminAnswerResponse;
import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminCardResponse;
import botobo.core.admin.dto.AdminCategoryRequest;
import botobo.core.admin.dto.AdminCategoryResponse;
import botobo.core.admin.ui.AdminController;
import botobo.core.quiz.exception.CardNotFoundException;
import botobo.core.quiz.exception.CategoryNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Test
    @DisplayName("관리자 카테고리 생성 - 성공")
    void createCategory() throws Exception {
        // given
        AdminCategoryRequest adminCategoryRequest = new AdminCategoryRequest("JAVA", "botobo.io", "프로그래밍 언어입니다.");
        given(adminService.createCategory(any())).willReturn(
                AdminCategoryResponse.builder()
                        .id(1L)
                        .name("JAVA")
                        .logoUrl("botobo.io")
                        .description("프로그래밍 언어입니다.")
                        .build()
        );

        // when, then
        mockMvc.perform(post("/admin/categories")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminCategoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/admin/categories/1"))
                .andDo(document("admin-categories-post-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    @Test
    @DisplayName("관리자 카드 생성 - 성공")
    void createCard() throws Exception {
        // given
        AdminCardRequest adminCardRequest = new AdminCardRequest("질문1", 1L);
        given(adminService.createCard(any())).willReturn(
                AdminCardResponse.builder()
                        .id(1L)
                        .question("질문1")
                        .categoryId(1L)
                        .build()
        );

        // when, then
        mockMvc.perform(post("/admin/cards")
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
    @DisplayName("관리자 카드 생성 - 실패, 카테고리 존재하지 않음")
    void createCardWithInvalidCategory() throws Exception {
        // given
        AdminCardRequest adminCardRequest = new AdminCardRequest("질문1", 1000L);
        given(adminService.createCard(any())).willThrow(new CategoryNotFoundException());

        // when, then
        mockMvc.perform(post("/admin/cards")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminCardRequest)))
                .andExpect(status().isNotFound())
                .andDo(document("admin-cards-post-fail-invalid-category",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    @Test
    @DisplayName("관리자 답변 생성 - 성공")
    void createAnswer() throws Exception {
        // given
        AdminAnswerRequest adminAnswerRequest = new AdminAnswerRequest("답변1", 1L);
        given(adminService.createAnswer(any())).willReturn(
                AdminAnswerResponse.builder()
                        .id(1L)
                        .content("답변1")
                        .cardId(1L)
                        .build()
        );

        // when, then
        mockMvc.perform(post("/admin/answers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminAnswerRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/admin/answers/1"))
                .andDo(document("admin-answers-post-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    @Test
    @DisplayName("관리자 정답 생성 - 실패, 카드 존재하지 않음")
    void createAnswerWithInvalidCard() throws Exception {
        // given
        AdminAnswerRequest adminAnswerRequest = new AdminAnswerRequest("정답1", 1000L);
        given(adminService.createAnswer(any())).willThrow(new CardNotFoundException());

        // when, then
        mockMvc.perform(post("/admin/answers")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminAnswerRequest)))
                .andExpect(status().isNotFound())
                .andDo(document("admin-answers-post-fail-invalid-card",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }
}
