package botobo.core.category.dto;

import botobo.core.category.domain.Category;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {

    private Long id;
    private String name;
    private String logoUrl;
    private String description;

    private CategoryDto(Long id, String name, String logoUrl, String description) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    public static CategoryDto of(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getLogoUrl(),
                category.getDescription()
        );
    }

    public static List<CategoryDto> listOf(List<Category> categories) {
        return categories.stream()
                .map(CategoryDto::of)
                .collect(Collectors.toList());
    }
}
