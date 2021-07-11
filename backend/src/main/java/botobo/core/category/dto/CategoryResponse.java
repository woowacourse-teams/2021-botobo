package botobo.core.category.dto;

import botobo.core.category.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private int cardCount;
    private String logoUrl;

    private CategoryResponse(Long id, String name, String description, int cardCount, String logoUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cardCount = cardCount;
        this.logoUrl = logoUrl;
    }

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.cardCount(),
                category.getLogoUrl()
        );
    }

    public static List<CategoryResponse> listOf(List<Category> categories) {
        return categories.stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }
}
