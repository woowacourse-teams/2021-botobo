package botobo.core.category.domain;

import botobo.core.card.domain.Card;
import botobo.core.card.domain.Cards;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false, length = 100)
    private String logoUrl = "";

    @Column(nullable = false)
    private String description = "";

    @Embedded
    private Cards cards = new Cards();

    @Builder
    public Category(Long id, String name, boolean isDeleted, String logoUrl,
                    String description) {
        validateNull(name, logoUrl, description);
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    private void validateNull(String name, String logoUrl, String description) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("Category의 Name에는 null이 들어갈 수 없습니다.");
        }
        if (Objects.isNull(logoUrl)) {
            throw new IllegalArgumentException("Category의 LogoUrl에는 null이 들어갈 수 없습니다.");
        }
        if (Objects.isNull(description)) {
            throw new IllegalArgumentException("Category의 Description에는 null이 들어갈 수 없습니다.");
        }
    }

    public int cardCount() {
        return cards.size();
    }

    public List<Card> getAllCards() {
        return cards.getCards();
    }
}

