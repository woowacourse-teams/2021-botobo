package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.CardRequest;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("카드 서비스 테스트")
@MockitoSettings
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private WorkbookRepository workbookRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    @DisplayName("카드 생성 - 성공")
    void createCard() {
        // given
        CardRequest cardRequest = cardRequest();
        Workbook workbook = workbook(1L);
        Card card = card(1L, workbook);

        given(workbookRepository.findById(cardRequest.getWorkbookId())).willReturn(Optional.of(workbook));
        given(cardRepository.save(any())).willReturn(card);

        // when
        cardService.createCard(cardRequest);

        // then
        then(workbookRepository)
                .should(times(1))
                .findById(cardRequest.getWorkbookId());
        then(cardRepository)
                .should(times(1))
                .save(any());
    }

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

    private Workbook workbook(Long id) {
        return Workbook.builder()
                .id(id)
                .name("workbook")
                .build();
    }

    private Card card(Long id, Workbook workbook) {
        return Card.builder()
                .id(id)
                .question("question")
                .answer("answer")
                .workbook(workbook)
                .build();
    }

    private List<Card> listOfThreeCards() {
        return List.of(
                card(1L, workbook(1L)),
                card(2L, workbook(1L)),
                card(3L, workbook(1L))
        );
    }

    private CardRequest cardRequest() {
        return CardRequest.builder()
                .question("question")
                .answer("answer")
                .workbookId(1L)
                .build();
    }

    private NextQuizCardsRequest nextQuizCardsRequestWithThreeIds() {
        return NextQuizCardsRequest.builder()
                .cardIds(List.of(1L, 2L, 3L))
                .build();
    }
}