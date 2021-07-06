package botobo.core.card.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Answer {

    private Long id;
    private String content;
    private boolean isDeleted;
    private Card card;

    @Builder
    public Answer(Long id, String content, boolean isDeleted, Card card) {
        this.id = id;
        this.content = content;
        this.isDeleted = isDeleted;
        this.card = card;
    }
}
