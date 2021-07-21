package botobo.core.quiz.domain.card;

import botobo.core.quiz.domain.workbook.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FixedCardsTest {

    private List<Card> cards;
    private List<Card> nineCards;
    private Card card;

    @BeforeEach
    void setUp() {
        card = Card.builder()
                .question("question")
                .answer("answer")
                .workbook(Workbook.builder().name("name").build())
                .build();
        cards = Arrays.asList(card, card, card, card, card, card, card, card, card, card);
        nineCards = Arrays.asList(card, card, card, card, card, card, card, card, card);
    }

    @Test
    @DisplayName("FixedCards의 getCards는 unmodifiable한 객체를 리턴한다. - 성공")
    void deepCopy() {
        // given
        FixedCards fixedCards = FixedCards.getInstance();
        fixedCards.initialize(cards);

        // when
        List<Card> copiedCards = fixedCards.getCards();
        assertThatThrownBy(() -> copiedCards.add(card))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("FixedCards는 항상 같은 인스턴스를 생성한다. - 성공")
    void isSameInstance() {
        // given
        FixedCards fixedCards = FixedCards.getInstance();

        // when
        fixedCards.initialize(cards);

        // then
        assertThat(FixedCards.getInstance()).isSameAs(fixedCards);
        assertThat(FixedCards.getInstance()).isEqualTo(fixedCards);
    }

    @Test
    @DisplayName("한번 초기화된 FixedCards는 다음 요청 시 동일한 카드를 반환한다. - 성공")
    void checkThatAlwaysSameFixedCards() {
        // given
        FixedCards fixedCards = FixedCards.getInstance();
        fixedCards.initialize(cards);

        // when
        FixedCards secondRequestedFixedCards = FixedCards.getInstance();

        // then
        assertThat(fixedCards).isSameAs(secondRequestedFixedCards);
    }

    @Test
    @DisplayName("비회원의 카드 개수는 10개로 한정한다. - 실패, 9개는 불가")
    void checkCardCount() {
        // given
        FixedCards fixedCards = FixedCards.getInstance();

        // when - then
        assertThatThrownBy(() -> fixedCards.initialize(nineCards)).isInstanceOf(IllegalArgumentException.class);
    }
}