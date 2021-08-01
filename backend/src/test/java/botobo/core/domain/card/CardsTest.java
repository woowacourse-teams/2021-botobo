package botobo.core.domain.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CardsTest {

    private List<Card> tenCards;

    @BeforeEach
    void setUp() {
        tenCards = CardFactory.createGuestCards();
    }

    @DisplayName("isNextQuiz가 false인 10개의 카드를 생성한다. - 성공")
    @Test
    void chooseCard() {
        // given
        Cards cards = new Cards(tenCards);

        // when
        final Cards chosenCards = cards.chooseCard(false, 10);

        // then
        assertThat(chosenCards.counts()).isEqualTo(10);
    }

    @DisplayName("isNextQuiz가 false이며 targetCount가 11개일 때, 10개의 카드를 생성한다. - 성공")
    @Test
    void chooseCardSizeWithLowerThanLimit() {
        // given
        Cards cards = new Cards(tenCards);

        // when
        final Cards chosenCards = cards.chooseCard(false, 11);

        // then
        assertThat(chosenCards.counts()).isEqualTo(10);
    }

    @DisplayName("isNextQuiz가 true인 10개의 카드를 생성한다. - 성공")
    @Test
    void chooseCardWithIsNextQuiz() {
        // given
        tenCards.forEach(Card::makeNextQuiz);
        Cards cards = new Cards(tenCards);
        // when
        final Cards chosenCards = cards.chooseCard(true, 10);

        // then
        assertThat(chosenCards.counts()).isEqualTo(10);
    }

    @DisplayName("마주친 횟수 증가 및 또보기 옵션 취소를 한다. - 성공")
    @Test
    void postProcess() {
        // given
        tenCards.forEach(Card::makeNextQuiz);
        Cards cards = new Cards(tenCards);

        // when
        cards.postProcess();

        // then
        final Cards chosenCards = cards.chooseCard(true, 10);
        assertThat(chosenCards.counts()).isEqualTo(0);
        for (Card card : tenCards) {
            assertThat(card.getEncounterCount()).isEqualTo(1);
        }
    }
}
