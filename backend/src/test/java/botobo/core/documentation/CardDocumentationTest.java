package botobo.core.documentation;

import botobo.core.auth.application.AuthService;
import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.quiz.application.CardService;
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

import static org.mockito.BDDMockito.given;
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
    private AuthService authService;

    @Test
    @DisplayName("또 보기 원하는 카드 선택 - 성공")
    void selectNextQuizCards() throws Exception {
        // given
        NextQuizCardsRequest request = NextQuizCardsRequest.builder()
                .cardIds(List.of(1L, 2L, 3L))
                .build();
        String token = "botobo.access.token";

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
