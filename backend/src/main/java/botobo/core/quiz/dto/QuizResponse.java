package botobo.core.quiz.dto;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.category.Category;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizResponse {

    private Long id;
    private String question;
    private String answer;
    private String categoryName;

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
