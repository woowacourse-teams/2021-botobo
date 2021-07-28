package botobo.core.documentation;

import botobo.core.application.AuthService;
import botobo.core.application.WorkbookService;
import botobo.core.dto.card.CardSimpleResponse;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
import botobo.core.ui.WorkbookController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("문제집 문서화 테스트")
@WebMvcTest(WorkbookController.class)
public class WorkbookDocumentationTest extends DocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkbookService workbookService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("유저가 문제집 추가 - 성공")
    void createWorkbookByUser() throws Exception {
        // given
        String token = "botobo.access.token";
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("Java 문제집")
                .opened(true)
                .build();
        WorkbookResponse workbookResponse = WorkbookResponse.builder()
                .id(1L)
                .name("Java 문제집")
                .opened(true)
                .cardCount(0)
                .build();
        given(workbookService.createWorkbookByUser(any(), any())).willReturn(workbookResponse);

        // when, then
        document()
                .mockMvc(mockMvc)
                .post("/api/workbooks", workbookRequest)
                .auth(token)
                .build()
                .status(status().isCreated())
                .identifier("workbooks-post-success");
    }

    @Test
    @DisplayName("문제집 전체 조회 - 성공")
    void findAllWorkbooks() throws Exception {
        // given
        String token = "botobo.access.token";
        given(workbookService.findWorkbooksByUser(any())).willReturn(generateWorkbookResponse());

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/workbooks")
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("workbooks-get-success");
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 - 성공")
    void findWorkbookCardsById() throws Exception {
        // given
        String token = "botobo.access.token";
        given(workbookService.findWorkbookCardsById(anyLong())).willReturn(generateWorkbookCardsResponse());

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/workbooks/{id}/cards", 1)
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("workbooks-cards-get-success");
    }

    @Test
    @DisplayName("유저가 문제집 수정 - 성공")
    void updateWorkbook() throws Exception {
        // given
        String token = "botobo.access.token";
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집")
                .opened(true)
                .cardCount(0)
                .build();
        WorkbookResponse workbookResponse = WorkbookResponse.builder()
                .id(1L)
                .name("Java 문제집 수정")
                .opened(false)
                .cardCount(0)
                .build();
        given(workbookService.updateWorkbook(anyLong(), any(), any())).willReturn(workbookResponse);

        // when, then
        document()
                .mockMvc(mockMvc)
                .put("/api/workbooks/{id}", workbookUpdateRequest, workbookResponse.getId())
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("workbooks-put-success");
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 성공")
    void deleteWorkbook() throws Exception {
        // given
        String token = "botobo.access.token";
        WorkbookResponse workbookResponse = WorkbookResponse.builder()
                .id(1L)
                .name("Java 문제집 수정")
                .opened(false)
                .cardCount(0)
                .build();

        // when, then
        document()
                .mockMvc(mockMvc)
                .delete("/api/workbooks/{id}", workbookResponse.getId())
                .auth(token)
                .build()
                .status(status().isNoContent())
                .identifier("workbooks-delete-success");
    }

    private List<WorkbookResponse> generateWorkbookResponse() {
        return Arrays.asList(
                WorkbookResponse.builder()
                        .id(1L)
                        .name("피케이의 자바 문제 20선")
                        .cardCount(20)
                        .author("pk")
                        .build(),
                WorkbookResponse.builder()
                        .id(2L)
                        .name("오즈의 비올 때 푸는 Database 문제")
                        .cardCount(15)
                        .author("oz")
                        .build(),
                WorkbookResponse.builder()
                        .id(3L)
                        .name("조앤의 Network 정복 모음집")
                        .cardCount(8)
                        .author("joanne")
                        .build()
        );
    }

    private WorkbookCardResponse generateWorkbookCardsResponse() {
        List<CardSimpleResponse> cardSimpleResponse = Arrays.asList(
                CardSimpleResponse.builder()
                        .id(1L)
                        .question("Java에는 a가 몇 개 들어갈까요?")
                        .answer("2개")
                        .bookmark(true)
                        .encounterCount(5)
                        .build(),
                CardSimpleResponse.builder()
                        .id(2L)
                        .question("Java에는 v가 몇 개 들어갈까요?")
                        .answer("1개")
                        .bookmark(true)
                        .encounterCount(10)
                        .build(),
                CardSimpleResponse.builder()
                        .id(3L)
                        .question("Java에는 j가 몇 개 들어갈까요?")
                        .answer("1개")
                        .bookmark(false)
                        .encounterCount(2)
                        .build());
        return WorkbookCardResponse.builder()
                .workbookId(1L)
                .workbookName("Java")
                .cards(cardSimpleResponse)
                .build();
    }
}
