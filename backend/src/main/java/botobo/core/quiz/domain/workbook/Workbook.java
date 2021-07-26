package botobo.core.quiz.domain.workbook;

import botobo.core.common.domain.BaseEntity;
import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.Cards;
import botobo.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Objects;


@Getter
@NoArgsConstructor
@Entity
public class Workbook extends BaseEntity {

    private static final int NAME_MAX_LENGTH = 30;

    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean opened;

    @Column(nullable = false)
    private boolean isDeleted;

    @Embedded
    private Cards cards = new Cards();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_workbook_to_user"))
    private User user;

    @Builder
    public Workbook(Long id, String name, boolean opened, boolean isDeleted, Cards cards, User user) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.opened = opened;
        this.isDeleted = isDeleted;
        this.user = user;
        if (Objects.nonNull(cards)) {
            this.cards = cards;
        }
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

    public String author() {
        if (Objects.isNull(user)) {
            return "존재하지 않는 유저";
        }
        return user.getUserName();
    }

    public int cardCount() {
        return cards.counts();
    }

    public List<Card> getCardsAsList() {
        return cards.getCards();
    }
}
