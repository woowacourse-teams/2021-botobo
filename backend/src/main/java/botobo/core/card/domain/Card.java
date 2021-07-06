package botobo.core.card.domain;

import botobo.core.category.domain.Category;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Card {

    private Long id;
    private String question;
    private boolean isDeleted;
    private Category category;

    @Builder
    public Card(Long id, String question, boolean isDeleted, Category category) {
        this.id = id;
        this.question = question;
        this.isDeleted = isDeleted;
        this.category = category;
    }
}
