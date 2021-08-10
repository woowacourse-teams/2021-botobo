package botobo.core.documentation;

import botobo.core.application.WorkbookService;
import botobo.core.domain.user.AppUser;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.card.ScrapCardRequest;
import botobo.core.dto.heart.HeartResponse;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
import botobo.core.ui.WorkbookController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("문제집 문서화 테스트")
@WebMvcTest(WorkbookController.class)
public class WorkbookDocumentationTest extends DocumentationTest {

    @MockBean
    private WorkbookService workbookService;

    @Test
    @DisplayName("유저가 문제집 추가 - 성공")
    void createWorkbookByUser() throws Exception {
        // given
        String token = obtainAuthenticatedToken();
        WorkbookRequest workbookRequest = WorkbookRequest.builder()
                .name("Java 문제집")
                .opened(true)
                .tags(Arrays.asList(
                        TagRequest.builder().id(0L).name("자바").build(),
                        TagRequest.builder().id(0L).name("java").build()
                ))
                .build();
        WorkbookResponse workbookResponse = WorkbookResponse.builder()
                .id(1L)
                .name("Java 문제집")
                .opened(true)
                .tags(Arrays.asList(
                        TagResponse.builder().id(1L).name("자바").build(),
                        TagResponse.builder().id(2L).name("java").build()
                ))
                .cardCount(0)
                .heartCount(0)
                .build();
        given(workbookService.createWorkbookByUser(any(WorkbookRequest.class), any(AppUser.class)))
                .willReturn(workbookResponse);

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
    @DisplayName("유저 문제집 전체 조회 - 성공")
    void findWorkbooksByUser() throws Exception {
        // given
        String token = obtainAuthenticatedToken();
        given(workbookService.findWorkbooksByUser(any(AppUser.class))).willReturn(generateUserWorkbookResponse());

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
    @DisplayName("비회원 문제집 조회 - 성공")
    void findWorkbooksByAnonymous() throws Exception {
        // given
        given(workbookService.findWorkbooksByUser(any(AppUser.class))).willReturn(Collections.emptyList());

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/workbooks")
                .build()
                .status(status().isOk())
                .identifier("workbooks-get-anonymous-success");
    }

    @Test
    @DisplayName("문제집의 카드 모아보기 - 성공")
    void findWorkbookCardsById() throws Exception {
        // given
        String token = obtainAuthenticatedToken();
        given(workbookService.findWorkbookCardsById(anyLong(), any(AppUser.class))).willReturn(generatePersonalWorkbookCardsResponse());

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
        String token = obtainAuthenticatedToken();
        WorkbookUpdateRequest workbookUpdateRequest = WorkbookUpdateRequest.builder()
                .name("Java 문제집 수정")
                .opened(true)
                .cardCount(0)
                .heartCount(0)
                .tags(Arrays.asList(
                        TagRequest.builder().id(1L).name("자바").build(),
                        TagRequest.builder().id(2L).name("java").build(),
                        TagRequest.builder().id(0L).name("stream").build()
                ))
                .build();
        WorkbookResponse workbookResponse = WorkbookResponse.builder()
                .id(1L)
                .name("Java 문제집 수정")
                .opened(true)
                .cardCount(0)
                .heartCount(0)
                .tags(Arrays.asList(
                        TagResponse.builder().id(1L).name("자바").build(),
                        TagResponse.builder().id(2L).name("java").build(),
                        TagResponse.builder().id(3L).name("stream").build()
                ))
                .build();
        given(workbookService.updateWorkbook(anyLong(), any(WorkbookUpdateRequest.class), any(AppUser.class)))
                .willReturn(workbookResponse);

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
    @DisplayName("공유 문제집 상세보기 - 성공")
    void findPublicWorkbookById() throws Exception {
        // given
        String token = obtainAuthenticatedToken();
        given(workbookService.findPublicWorkbookById(anyLong(), any(AppUser.class))).willReturn(generatePublicWorkbookCardsResponse());

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/workbooks/public/{id}", 1)
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("workbooks-public-get-success");
    }

    @Test
    @DisplayName("유저가 문제집 삭제 - 성공")
    void deleteWorkbook() throws Exception {
        // given
        String token = obtainAuthenticatedToken();
        WorkbookResponse workbookResponse = WorkbookResponse.builder()
                .id(1L)
                .name("Java 문제집 수정")
                .opened(false)
                .cardCount(0)
                .heartCount(0)
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

    @Test
    @DisplayName("문제집으로 카드 가져오기 - 성공")
    void scrapSelectedCardsToWorkbook() throws Exception {
        // given
        String token = obtainAuthenticatedToken();
        long workbookId = 1L;
        ScrapCardRequest scrapCardRequest = ScrapCardRequest.builder()
                .cardIds(Arrays.asList(10L, 11L, 12L))
                .build();

        // when, then
        document()
                .mockMvc(mockMvc)
                .post("/api/workbooks/{id}/cards", scrapCardRequest, workbookId)
                .auth(token)
                .locationHeader("/api/workbooks/1/cards")
                .build()
                .status(status().isCreated())
                .identifier("workbooks-scrap-cards-success");
    }

    @Test
    @DisplayName("유저가 하트 토글 - 성공")
    void toggleHeart() throws Exception {
        // given
        String token = obtainAuthenticatedToken();
        HeartResponse heartResponse = HeartResponse.of(true);

        given(workbookService.toggleHeart(anyLong(), any(AppUser.class))).willReturn(heartResponse);

        // when, then
        document()
                .mockMvc(mockMvc)
                .putWithoutBody("/api/workbooks/{id}/hearts", 1L)
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("workbooks-toggle-hearts-success");
    }

    private List<WorkbookResponse> generateUserWorkbookResponse() {
        return Arrays.asList(
                WorkbookResponse.builder()
                        .id(1L)
                        .name("피케이의 자바 문제 20선")
                        .cardCount(20)
                        .heartCount(10)
                        .opened(false)
                        .tags(Arrays.asList(
                                TagResponse.builder().id(1L).name("자바").build(),
                                TagResponse.builder().id(2L).name("java").build()
                        ))
                        .build(),
                WorkbookResponse.builder()
                        .id(2L)
                        .name("피케이의 비올 때 푸는 Database 문제")
                        .cardCount(15)
                        .heartCount(30)
                        .opened(true)
                        .tags(Arrays.asList(
                                TagResponse.builder().id(3L).name("데이터베이스").build(),
                                TagResponse.builder().id(4L).name("DB").build()
                        ))
                        .build(),
                WorkbookResponse.builder()
                        .id(3L)
                        .name("피케이의 Network 정복 모음집")
                        .cardCount(8)
                        .heartCount(1)
                        .opened(true)
                        .tags(Arrays.asList(
                                TagResponse.builder().id(5L).name("network").build()
                        ))
                        .build()
        );
    }

    private List<CardResponse> generateCardResponses() {
        return Arrays.asList(
                CardResponse.builder()
                        .id(1L)
                        .question("Java에는 a가 몇 개 들어갈까요?")
                        .answer("2개")
                        .bookmark(true)
                        .encounterCount(5)
                        .nextQuiz(false)
                        .workbookId(1L)
                        .build(),
                CardResponse.builder()
                        .id(2L)
                        .question("Java에는 v가 몇 개 들어갈까요?")
                        .answer("1개")
                        .bookmark(true)
                        .encounterCount(10)
                        .nextQuiz(true)
                        .workbookId(1L)
                        .build(),
                CardResponse.builder()
                        .id(3L)
                        .question("Java에는 j가 몇 개 들어갈까요?")
                        .answer("1개")
                        .bookmark(false)
                        .encounterCount(2)
                        .nextQuiz(true)
                        .workbookId(1L)
                        .build());
    }

    private List<TagResponse> generateTagResponses() {
        return Arrays.asList(
                TagResponse.builder()
                        .id(1L)
                        .name("java")
                        .build(),
                TagResponse.builder()
                        .id(2L)
                        .name("spring")
                        .build()
        );
    }

    private WorkbookCardResponse generatePersonalWorkbookCardsResponse() {
        return WorkbookCardResponse.builder()
                .workbookId(1L)
                .workbookName("Java")
                .cards(generateCardResponses())
                .build();
    }

    private WorkbookCardResponse generatePublicWorkbookCardsResponse() {
        return WorkbookCardResponse.builder()
                .workbookId(1L)
                .workbookName("자바의 정석")
                .cardCount(3)
                .heartCount(100)
                .heart(true)
                .tags(generateTagResponses())
                .cards(generateCardResponses())
                .build();
    }

    private List<WorkbookResponse> generatePublicWorkbookResponse() {
        return Arrays.asList(
                WorkbookResponse.builder()
                        .id(1L)
                        .name("피케이의 Network 정복 모음집")
                        .cardCount(8)
                        .heartCount(314)
                        .author("피케이")
                        .tags(Arrays.asList(
                                TagResponse.builder().id(1L).name("네트워크").build(),
                                TagResponse.builder().id(2L).name("network").build()
                        ))
                        .build(),
                WorkbookResponse.builder()
                        .id(2L)
                        .name("오즈의 Network 정복 최종판")
                        .cardCount(20)
                        .heartCount(100)
                        .author("오즈")
                        .tags(Arrays.asList(
                                TagResponse.builder().id(3L).name("네트워크").build(),
                                TagResponse.builder().id(4L).name("천재").build()
                        ))
                        .build()
        );
    }
}
