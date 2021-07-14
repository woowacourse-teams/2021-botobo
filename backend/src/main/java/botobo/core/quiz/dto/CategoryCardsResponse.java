package botobo.core.quiz.dto;

import botobo.core.quiz.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryCardsResponse {

    private String categoryName;
    private List<CardResponse> cards;

    @Builder
    private CategoryCardsResponse(String categoryName, List<CardResponse> cards) {
        this.categoryName = categoryName;
        this.cards = cards;
    }

    public static CategoryCardsResponse of(Category category) {
        List<CardResponse> cardResponses = CardResponse.listOf(category.getCards());
        return CategoryCardsResponse.builder()
                .categoryName(category.getName())
                .cards(cardResponses)
                .build();
    }
}
