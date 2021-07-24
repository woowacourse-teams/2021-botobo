package botobo.core.documentation;

import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.quiz.application.CardService;
import botobo.core.quiz.dto.CardRequest;
import botobo.core.quiz.dto.CardResponse;
import botobo.core.quiz.dto.CardUpdateRequest;
import botobo.core.quiz.dto.CardUpdateResponse;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import botobo.core.quiz.ui.CardController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static botobo.core.documentation.DocumentationUtils.getDocumentRequest;
import static botobo.core.documentation.DocumentationUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("카드 문서화 테스트")
@WebMvcTest(CardController.class)
public class CardDocumentationTest extends DocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardService cardService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("카드 생성 - 성공")
    void createCard() throws Exception {
        // given
        CardRequest request = CardRequest.builder()
                .question("이것은 질문입니다!")
                .answer("그리고 답변입니다~")
                .workbookId(1L)
                .build();
        CardResponse response = CardResponse.builder()
                .id(1L)
                .question("이것은 질문입니다!")
                .answer("그리고 답변입니다~")
                .workbookId(1L)
                .encounterCount(0)
                .bookmark(false)
                .nextQuiz(false)
                .build();
        String token = "botobo.access.token";
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);
        given(cardService.createCard(any())).willReturn(response);

        // when, then
        mockMvc.perform(post("/api/cards")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("cards-post-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    @Test
    @DisplayName("카드 수정 - 성공")
    void updateCard() throws Exception {
        // given
        CardUpdateRequest request = CardUpdateRequest.builder()
                .question("수정된 질문입니다!")
                .answer("그리고 수정된 답변입니다~")
                .bookmark(true)
                .build();
        CardUpdateResponse response = CardUpdateResponse.builder()
                .id(1L)
                .question("수정된 질문입니다!")
                .answer("그리고 수정된 답변입니다~")
                .workbookId(1L)
                .bookmark(true)
                .build();
        String token = "botobo.access.token";
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);
        given(cardService.updateCard(anyLong(), any())).willReturn(response);

        // when, then
        mockMvc.perform(put("/api/cards/1")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("cards-put-success",
                        getDocumentRequest(),
                        getDocumentResponse())
                );
    }

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 성공")
    void selectNextQuizCards() throws Exception {
        // given
        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(List.of(1L, 2L, 3L))
                .build();
        String token = "botobo.access.token";
        given(jwtTokenProvider.isValidToken(token)).willReturn(true);

        // when, then
        document()
                .mockMvc(mockMvc)
                .put("/api/cards/next-quiz", request)
                .auth(token)
                .build()
                .status(status().isNoContent())
                .identifier("cards-next-quiz-put-success");
    }
}
