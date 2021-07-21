package botobo.core.quiz.domain.workbook;

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


@Getter
@NoArgsConstructor
@Entity
public class Workbook extends BaseEntity {

    private static final int NAME_MAX_LENGTH = 30;

    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Column(nullable = false)
    private boolean isDeleted;

    @Embedded
    private Cards cards = new Cards();

    @Column(nullable = false)
    private boolean isPublic;

    @Builder
    public Workbook(Long id, String name, boolean isDeleted, boolean isPublic) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
        this.isPublic = isPublic;
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("Workbook의 Name에는 null이 들어갈 수 없습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Workbook의 Name에는 비어있거나 공백 문자열이 들어갈 수 없습니다.");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Workbook의 Name %d자 이하여야 합니다.", NAME_MAX_LENGTH)
            );
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
