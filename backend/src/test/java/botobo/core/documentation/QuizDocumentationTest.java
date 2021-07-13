package botobo.core.documentation;

import botobo.core.quiz.application.QuizService;
import botobo.core.quiz.dto.CategoryIdsRequest;
import botobo.core.quiz.dto.QuizResponse;
import botobo.core.quiz.exception.CategoryNotFoundException;
import botobo.core.quiz.ui.QuizController;
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

import java.util.Arrays;
import java.util.List;

import static botobo.core.documentation.DocumentationUtils.getDocumentRequest;
import static botobo.core.documentation.DocumentationUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class QuizDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuizService quizService;

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 성공")
    void createQuiz() throws Exception {
        // given
        CategoryIdsRequest categoryIdsRequest = new CategoryIdsRequest(Arrays.asList(1L, 2L, 3L));
        given(quizService.createQuiz(Arrays.asList(1L, 2L, 3L))).willReturn(generateQuizResponses());

        // when, then
        mockMvc.perform(post("/quizzes")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(categoryIdsRequest)))
                .andExpect(status().isOk())
                .andDo(document("quizzes-post-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    @Test
    @DisplayName("카테고리 id(Long)를 이용해서 퀴즈 생성 - 실패, 카테고리 존재하지 않음")
    void createQuizWithInvalidCategoryId() throws Exception {
        // given
        CategoryIdsRequest categoryIdsRequest = new CategoryIdsRequest(Arrays.asList(1L, 2L, 1000L));
        given(quizService.createQuiz(Arrays.asList(1L, 2L, 1000L))).willThrow(new CategoryNotFoundException());

        // when, then
        mockMvc.perform(post("/quizzes")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(categoryIdsRequest)))
                .andExpect(status().isNotFound())
                .andDo(document("quizzes-post-fail-invalid-category-id",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    private List<QuizResponse> generateQuizResponses() {
        return Arrays.asList(
                QuizResponse.builder()
                        .id(3L)
                        .question("Javascript 질문 2")
                        .answer("Javascript 답 2")
                        .categoryName("Javascript")
                        .build(),
                QuizResponse.builder()
                        .id(10L)
                        .question("Java 질문 1")
                        .answer("Java 답 1")
                        .categoryName("JAVA")
                        .build(),
                QuizResponse.builder()
                        .id(2L)
                        .question("Java 질문 3")
                        .answer("Java 답 3")
                        .categoryName("JAVA")
                        .build(),
                QuizResponse.builder()
                        .id(11L)
                        .question("Spring 질문 1")
                        .answer("Java 답 1")
                        .categoryName("JAVA")
                        .build(),
                QuizResponse.builder()
                        .id(1L)
                        .question("Java 질문 2")
                        .answer("Java 답 2")
                        .categoryName("JAVA")
                        .build(),
                QuizResponse.builder()
                        .id(4L)
                        .question("Spring 질문 6")
                        .answer("Spring 답 6")
                        .categoryName("Spring")
                        .build(),
                QuizResponse.builder()
                        .id(7L)
                        .question("Javascript 질문 9")
                        .answer("Javascript 답 9")
                        .categoryName("Javascript")
                        .build(),
                QuizResponse.builder()
                        .id(21L)
                        .question("Spring 질문 3")
                        .answer("Spring 답 3")
                        .categoryName("Spring")
                        .build(),
                QuizResponse.builder()
                        .id(17L)
                        .question("Javascript 질문 4")
                        .answer("Javascript 답 4")
                        .categoryName("Javascript")
                        .build(),
                QuizResponse.builder()
                        .id(14L)
                        .question("Javascript 질문 1")
                        .answer("Javascript 답 1")
                        .categoryName("Javascript")
                        .build()
        );
    }
}
