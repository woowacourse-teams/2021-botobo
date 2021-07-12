package botobo.core.admin.dto;

import botobo.core.quiz.domain.card.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminCardResponse {
    private Long id;
    private String question;
    private Long categoryId;

    private AdminCardResponse(Long id, String question, Long categoryId) {
        this.id = id;
        this.question = question;
        this.categoryId = categoryId;
    }

    public static AdminCardResponse of(Card card) {
        return new AdminCardResponse(
                card.getId(),
                card.getQuestion(),
                card.getCategory().getId());
    }
}
