package botobo.core.domain.card;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@Embeddable
public class Cards {

    @OneToMany(mappedBy = "workbook", cascade = CascadeType.PERSIST)
    private List<Card> cards = new ArrayList<>();

    public Cards(List<Card> cards) {
        this.cards = cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public int counts() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void addAll(Cards cards) {
        this.cards.addAll(cards.getCards());
    }

    public Cards chooseCard(boolean isNextQuiz, int targetCounts) {
        return new Cards(cards.stream()
                .filter(card -> card.equalsNextQuizWith(isNextQuiz))
                .limit(targetCounts)
                .collect(Collectors.toList()));
    }

    public Cards postProcess() {
        incrementEncounterCount();
        cancelNextQuiz();
        return this;
    }

    private void incrementEncounterCount() {
        cards.forEach(Card::incrementEncounterCount);
    }

    private void cancelNextQuiz() {
        cards.forEach(Card::cancelNextQuiz);
    }

    public Stream<Card> stream() {
        return cards.stream();
    }

    public void delete() {
        cards.forEach(Card::delete);
    }
}
