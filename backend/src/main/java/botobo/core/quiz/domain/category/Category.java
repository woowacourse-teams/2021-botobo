package botobo.core.quiz.domain.category;

import botobo.core.quiz.domain.BaseEntity;
import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.Cards;
import botobo.core.quiz.exception.CardNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class Category extends BaseEntity {

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean isDeleted;

    @Embedded
    private Cards cards = new Cards();

    @Builder
    public Category(Long id, String name, boolean isDeleted) {
        validateNull(name);
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    private void validateNull(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("Category의 Name에는 null이 들어갈 수 없습니다.");
        }
    }

    public int cardCount() {
        return cards.size();
    }

    public List<Card> getAllCards() {
        if (cards.isEmpty()) {
            throw new CardNotFoundException("카드가 존재하지 않습니다.");
        }
        return cards.getCards();
    }
}

