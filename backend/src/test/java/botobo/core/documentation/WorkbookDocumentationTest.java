package botobo.core.documentation;

import botobo.core.application.AuthService;
import botobo.core.application.WorkbookService;
import botobo.core.domain.user.AppUser;
import botobo.core.dto.card.CardSimpleResponse;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.WorkbookController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
    @DisplayName("비회원 문제집 조회 - 성공")
    void findWorkbooksByAnonymous() throws Exception {
        // given
        given(workbookService.findWorkbooksByUser(any()))
                .willReturn(Collections.emptyList());

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/workbooks")
                .build()
                .status(status().isOk())
                .identifier("workbooks-get-anonymous-success");
    }

    @Test
    @DisplayName("유저 문제집 조회 - 성공")
    void findWorkbooks() throws Exception {
        // given
        String token = "botobo.access.token";
        given(workbookService.findWorkbooksByUser(any())).willReturn(generateUserWorkbookResponse());

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
    @DisplayName("공유 문제집 검색 - 성공")
    void findPublicWorkbooksBySearch() throws Exception {
        // given
        String token = "botobo.access.token";
        given(workbookService.findPublicWorkbooksBySearch(anyString())).willReturn(generatePublicWorkbookResponse());

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/workbooks/public?search=Network")
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("workbooks-public-search-get-success");
    }

    @Test
    @DisplayName("키워드 없이 공유 문제집 검색 - 성공")
    void findPublicWorkbooksByNoKeywordSearch() throws Exception {
        // given
        String token = "botobo.access.token";
        given(workbookService.findPublicWorkbooksBySearch(anyString())).willReturn(generatePublicDummyWorkbookResponse());

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/workbooks/public")
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("workbooks-public-search-dummy-get-success");
    }

    private List<WorkbookResponse> generateUserWorkbookResponse() {
        return Arrays.asList(
                WorkbookResponse.builder()
                        .id(1L)
                        .name("피케이의 자바 문제 20선")
                        .cardCount(20)
                        .author("피케이")
                        .opened(false)
                        .build(),
                WorkbookResponse.builder()
                        .id(2L)
                        .name("피케이의 비올 때 푸는 Database 문제")
                        .cardCount(15)
                        .author("피케이")
                        .opened(true)
                        .build(),
                WorkbookResponse.builder()
                        .id(3L)
                        .name("피케이의 Network 정복 모음집")
                        .cardCount(8)
                        .author("피케이")
                        .opened(true)
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

    private List<WorkbookResponse> generatePublicWorkbookResponse() {
        return Arrays.asList(
                WorkbookResponse.builder()
                        .id(3L)
                        .name("피케이의 Network 정복 모음집")
                        .cardCount(8)
                        .author("피케이")
                        .opened(true)
                        .build(),
                WorkbookResponse.builder()
                        .id(4L)
                        .name("오즈의 Network 정복 최종판")
                        .cardCount(20)
                        .author("오즈")
                        .opened(true)
                        .build()
        );
    }

    private List<WorkbookResponse> generatePublicDummyWorkbookResponse() {
        return Arrays.asList(
                WorkbookResponse.builder()
                        .id(1L)
                        .name("자바 기본편")
                        .cardCount(20)
                        .author("보또보")
                        .opened(true)
                        .build(),
                WorkbookResponse.builder()
                        .id(2L)
                        .name("자바 심화편")
                        .cardCount(20)
                        .author("보또보")
                        .opened(true)
                        .build()
        );
    }
}
