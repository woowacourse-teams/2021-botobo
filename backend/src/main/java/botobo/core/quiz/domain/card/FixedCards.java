package botobo.core.quiz.domain.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FixedCards {
    private static FixedCards fixedCards;
    private static List<Card> cards;

    private FixedCards(List<Card> cards) {
        FixedCards.cards = cards;
    }

    public static FixedCards getInstance() {
        if (Objects.isNull(fixedCards) && Objects.isNull(cards)) {
            cards = new ArrayList<>();
            fixedCards = new FixedCards(cards);
        }
        return fixedCards;
    }

    public boolean isFull() {
        return !cards.isEmpty();
    }

    public List<Card> getCards() {
        return List.copyOf(cards);
    }

    public void initialize(List<Card> quiz) {
        if (quiz.size() != 10) {
            throw new IllegalArgumentException("비회원용 퀴즈에 포함될 카드의 개수는 10개여야합니다.");
        }
        if (cards.isEmpty()) {
            cards.addAll(quiz);
        }
    }
}
