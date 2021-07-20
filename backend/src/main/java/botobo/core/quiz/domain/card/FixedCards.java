package botobo.core.quiz.domain.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FixedCards {
    private static final FixedCards fixedCards = new FixedCards();
    private final List<Card> cards;

    private FixedCards() {
        cards = new ArrayList<>();
    }

    public static FixedCards getInstance() {
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
