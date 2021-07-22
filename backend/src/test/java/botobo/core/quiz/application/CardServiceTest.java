package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("카드 서비스 테스트")
@MockitoSettings
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    @DisplayName("다음에 또 보는 카드 선택 - 성공")
    void selectNextQuizCards() {
        // given
        NextQuizCardsRequest requestWithThreeIds = nextQuizCardsRequestWithThreeIds();
        List<Card> threeCards = listOfThreeCards();
        given(cardRepository.findByIdIn(requestWithThreeIds.getCardIds())).willReturn(threeCards);

        // when
        cardService.selectNextQuizCards(requestWithThreeIds);

        // then
        assertThat(threeCards).extracting("isNextQuiz")
                .containsExactly(true, true, true);
        then(cardRepository)
                .should(times(1))
                .findByIdIn(anyList());
    }

    private List<Card> listOfThreeCards() {
        Workbook workbook = Workbook.builder()
                .name("workbook")
                .build();
        return List.of(
                card(1L, workbook),
                card(2L, workbook),
                card(3L, workbook)
        );
    }

    private Card card(Long id, Workbook workbook) {
        return Card.builder()
                .id(id)
                .question("question")
                .answer("answer")
                .workbook(workbook)
                .build();
    }

    private NextQuizCardsRequest nextQuizCardsRequestWithThreeIds() {
        return NextQuizCardsRequest.builder()
                .cardIds(List.of(1L, 2L, 3L))
                .build();
    }
}