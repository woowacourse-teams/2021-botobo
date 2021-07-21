package botobo.core.quiz.dto;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<QuizResponse> listOf(List<Card> cards) {
        return cards.stream()
                .map(QuizResponse::of)
                .collect(Collectors.toList());
    }
}
