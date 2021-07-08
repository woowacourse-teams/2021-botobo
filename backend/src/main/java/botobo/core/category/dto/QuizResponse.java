package botobo.core.category.dto;

import botobo.core.card.domain.Card;
import botobo.core.category.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizResponse {
    private Long id;
    private String question;
    private String answer;
    private String categoryName;

    @Builder
    private QuizResponse(Long id, String question, String answer, String categoryName) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.categoryName = categoryName;
    }

    public static QuizResponse of(Card card) {
        final Category category = card.getCategory();
        final String answer = card.getAnswers().get(0).getContent();
        return QuizResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(answer)
                .categoryName(category.getName())
                .build();
    }
}
