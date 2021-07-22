package botobo.core.documentation;

import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.quiz.application.WorkbookService;
import botobo.core.quiz.dto.CardResponse;
import botobo.core.quiz.dto.WorkbookCardResponse;
import botobo.core.quiz.dto.WorkbookResponse;
import botobo.core.quiz.ui.WorkbookController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

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
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("문제집 전체 조회 - 성공")
    void findAllWorkbooks() throws Exception {
        // given
        String token = "botobo.access.token";
        given(workbookService.findAll()).willReturn(generateWorkbookResponse());
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/workbooks")
                .auth(token)
                .build()
                .addStatusAndIdentifier(status().isOk(), "workbooks-get-success");
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 - 성공")
    void findWorkbookCardsById() throws Exception {
        // given
        String token = "botobo.access.token";
        given(workbookService.findWorkbookCardsById(anyLong())).willReturn(generateWorkbookCardsResponse());
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/workbooks/{id}/cards", 1)
                .auth(token)
                .build()
                .addStatusAndIdentifier(status().isOk(), "workbooks-cards-get-success");

    }

    private List<WorkbookResponse> generateWorkbookResponse() {
        return Arrays.asList(
                WorkbookResponse.builder()
                        .id(1L)
                        .name("피케이의 자바 문제 20선")
                        .cardCount(20)
                        .build(),
                WorkbookResponse.builder()
                        .id(2L)
                        .name("오즈의 비올 때 푸는 Database 문제")
                        .cardCount(15)
                        .build(),
                WorkbookResponse.builder()
                        .id(3L)
                        .name("조앤의 Network 정복 모음집")
                        .cardCount(8)
                        .build()
        );
    }

    private WorkbookCardResponse generateWorkbookCardsResponse() {
        List<CardResponse> cardResponses = Arrays.asList(
                CardResponse.builder()
                        .id(1L)
                        .question("Java에는 a가 몇 개 들어갈까요?")
                        .answer("2개")
                        .build(),
                CardResponse.builder()
                        .id(2L)
                        .question("Java에는 v가 몇 개 들어갈까요?")
                        .answer("1개")
                        .build(),
                CardResponse.builder()
                        .id(3L)
                        .question("Java에는 j가 몇 개 들어갈까요?")
                        .answer("1개")
                        .build());
        return WorkbookCardResponse.builder()
                .workbookName("Java")
                .cards(cardResponses)
                .build();
    }
}
