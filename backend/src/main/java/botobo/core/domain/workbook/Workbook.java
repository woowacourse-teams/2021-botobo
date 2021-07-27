package botobo.core.domain.workbook;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.card.Card;
import botobo.core.domain.card.Cards;
import botobo.core.domain.user.User;
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

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean deleted;

    @Embedded
    private Cards cards = new Cards();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_workbook_to_user"))
    private User user;

    @Builder
    public Workbook(Long id, String name, boolean opened, boolean deleted, Cards cards, User user) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.opened = opened;
        this.deleted = deleted;
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

    public boolean containsWord(String word) {
        return name.toLowerCase()
                .contains(word.toLowerCase());
    }

    public boolean isPrivate() {
        return !isOpened();
    }

    public boolean ownedByAdmin() {
        return user.isAdmin();
    }

    public boolean ownedByUser() {
        return user.isUser();
    }

    public int cardCount() {
        return cards.counts();
    }

    public List<Card> getCardsAsList() {
        return cards.getCards();
    }
}