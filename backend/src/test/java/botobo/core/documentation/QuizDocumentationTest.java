package botobo.core.documentation;

import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.quiz.application.QuizService;
import botobo.core.quiz.dto.QuizRequest;
import botobo.core.quiz.dto.QuizResponse;
import botobo.core.quiz.exception.QuizEmptyException;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import botobo.core.quiz.ui.QuizController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("퀴즈 문서화 테스트")
@WebMvcTest(QuizController.class)
public class QuizDocumentationTest extends DocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 성공")
    void createQuiz() throws Exception {
        // given
        QuizRequest quizRequest = new QuizRequest(Arrays.asList(1L, 2L, 3L));
        String token = "botobo.access.token";
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);
        given(quizService.createQuiz(Arrays.asList(1L, 2L, 3L))).willReturn(generateQuizResponses());

        // when, then
        document()
                .mockMvc(mockMvc)
                .post("/api/quizzes", quizRequest)
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("quizzes-post-success");
    }

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 실패, 문제집이 존재하지 않음")
    void createQuizWithInvalidCategoryId() throws Exception {
        // given
        String token = "botobo.access.token";
        QuizRequest quizRequest = new QuizRequest(Arrays.asList(1L, 2L, 1000L));
        given(quizService.createQuiz(Arrays.asList(1L, 2L, 1000L))).willThrow(new WorkbookNotFoundException());
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);

        // when, then
        document()
                .mockMvc(mockMvc)
                .post("/api/quizzes", quizRequest)
                .auth(token)
                .build()
                .status(status().isNotFound())
                .identifier("quizzes-post-fail-invalid-category-id");
    }

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 실패, 퀴즈에 카드가 존재하지 않음.")
    void createQuizWithEmptyCards() throws Exception {
        // given
        String token = "botobo.access.token";
        QuizRequest quizRequest = new QuizRequest(Collections.singletonList(100L));
        given(quizService.createQuiz(Collections.singletonList(100L))).willThrow(new QuizEmptyException());
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);

        // when, then
        document()
                .mockMvc(mockMvc)
                .post("/api/quizzes", quizRequest)
                .auth(token)
                .build()
                .status(status().isBadRequest())
                .identifier("quizzes-post-fail-empty-cards");
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 성공")
    void createQuizFromWorkbook() throws Exception {
        // given
        String token = "botobo.access.token";
        Long workbookId = 1L;
        given(quizService.createQuizFromWorkbook(workbookId)).willReturn(generateQuizResponses());
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/quizzes/{workbookId}", workbookId)
                .auth(token)
                .build()
                .status(status().isOk())
                .identifier("quizzes-from-workbook-get-success");
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 문제집 아이디 없음")
    void createQuizFromWorkbookFailedWhenIdNotFound() throws Exception {
        // given
        String token = "botobo.access.token";
        Long workbookId = 100L;
        given(quizService.createQuizFromWorkbook(workbookId)).willThrow(WorkbookNotFoundException.class);
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/quizzes/{workbookId}", workbookId)
                .auth(token)
                .build()
                .status(status().isNotFound())
                .identifier("quizzes-from-workbook-get-fail-id-not-found");
    }

    @Test
    @DisplayName("문제집에서 바로 풀기 - 실패, 퀴즈에 문제가 존재하지 않음")
    void createQuizFromWorkbookFailedWhenQuizIsEmpty() throws Exception {
        // given
        String token = "botobo.access.token";
        Long workbookId = 1L;
        given(quizService.createQuizFromWorkbook(workbookId)).willThrow(QuizEmptyException.class);
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/quizzes/{workbookId}", workbookId)
                .auth(token)
                .build()
                .status(status().isBadRequest())
                .identifier("quizzes-from-workbook-get-fail-quiz-empty");
    }

    @Test
    @DisplayName("비회원용 퀴즈 생성 - 성공")
    void createQuizForGuest() throws Exception {
        // given
        given(quizService.createQuizForGuest()).willReturn(generateQuizResponses());

        // when, then
        document()
                .mockMvc(mockMvc)
                .get("/api/quizzes/guest")
                .build()
                .status(status().isOk())
                .identifier("quizzes-guest-get-success");
    }

    private List<QuizResponse> generateQuizResponses() {
        return Arrays.asList(
                QuizResponse.builder()
                        .id(3L)
                        .question("Javascript 질문 2")
                        .answer("Javascript 답 2")
                        .workbookName("Javascript")
                        .build(),
                QuizResponse.builder()
                        .id(10L)
                        .question("Java 질문 1")
                        .answer("Java 답 1")
                        .workbookName("JAVA")
                        .build(),
                QuizResponse.builder()
                        .id(2L)
                        .question("Java 질문 3")
                        .answer("Java 답 3")
                        .workbookName("JAVA")
                        .build(),
                QuizResponse.builder()
                        .id(11L)
                        .question("Spring 질문 1")
                        .answer("Java 답 1")
                        .workbookName("JAVA")
                        .build(),
                QuizResponse.builder()
                        .id(1L)
                        .question("Java 질문 2")
                        .answer("Java 답 2")
                        .workbookName("JAVA")
                        .build(),
                QuizResponse.builder()
                        .id(4L)
                        .question("Spring 질문 6")
                        .answer("Spring 답 6")
                        .workbookName("Spring")
                        .build(),
                QuizResponse.builder()
                        .id(7L)
                        .question("Javascript 질문 9")
                        .answer("Javascript 답 9")
                        .workbookName("Javascript")
                        .build(),
                QuizResponse.builder()
                        .id(21L)
                        .question("Spring 질문 3")
                        .answer("Spring 답 3")
                        .workbookName("Spring")
                        .build(),
                QuizResponse.builder()
                        .id(17L)
                        .question("Javascript 질문 4")
                        .answer("Javascript 답 4")
                        .workbookName("Javascript")
                        .build(),
                QuizResponse.builder()
                        .id(14L)
                        .question("Javascript 질문 1")
                        .answer("Javascript 답 1")
                        .workbookName("Javascript")
                        .build()
        );
    }
}
