package botobo.core.quiz.dto;

import botobo.core.quiz.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryCardsResponse {

    private String categoryName;
    private List<CardResponse> cards;

    public static CategoryCardsResponse of(Category category) {
        List<CardResponse> cardResponses = CardResponse.listOf(category.getCards());
        return CategoryCardsResponse.builder()
                .categoryName(category.getName())
                .cards(cardResponses)
                .build();
    }
}
