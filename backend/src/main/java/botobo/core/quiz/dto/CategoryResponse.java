package botobo.core.quiz.dto;

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
public class CategoryResponse {

    private Long id;
    private String name;
    private int cardCount;

    public static CategoryResponse of(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .cardCount(category.cardCount())
                .build();
    }

    public static List<CategoryResponse> listOf(List<Category> categories) {
        return categories.stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }
}
