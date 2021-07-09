package botobo.core.category.dto;

import botobo.core.category.domain.Category;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private String logoUrl;
    private String description;

    private CategoryResponse(Long id, String name, String logoUrl, String description) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getLogoUrl(),
                category.getDescription()
        );
    }

    public static List<CategoryResponse> listOf(List<Category> categories) {
        return categories.stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }
}
