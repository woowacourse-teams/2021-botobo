package botobo.core.domain.card;

import botobo.core.exception.card.QuizEmptyException;

import java.util.List;

public class Quiz {
    private final Cards cards;
    private final int targetCounts;


    public Quiz(List<Card> cards, int targetCounts) {
        this.cards = new Cards(cards);
        this.targetCounts = targetCounts;
    }

    public Cards makeQuiz() {
        cards.shuffle();
        Cards quiz = cards.chooseCard(true, targetCounts);
        if (isInsufficientCounts(quiz.counts())) {
            quiz.addAll(cards.chooseCard(false, remainCounts(quiz.counts())));
        }
        validateQuizIsEmpty(quiz);
        return quiz;
    }

    private boolean isInsufficientCounts(int preparedCounts) {
        return preparedCounts < targetCounts;
    }

    private int remainCounts(int preparedCounts) {
        return targetCounts - preparedCounts;
    }

    private void validateQuizIsEmpty(Cards quiz) {
        if (quiz.isEmpty()) {
            throw new QuizEmptyException();
        }
    }
}
