package botobo.core.quiz.dto;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.category.Category;
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
        final String answer = card.getFirstAnswerContent();
        return QuizResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(answer)
                .categoryName(category.getName())
                .build();
    }
}
