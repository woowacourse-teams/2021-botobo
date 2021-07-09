package botobo.core.admin.dto;

import botobo.core.category.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminCategoryRequest {
    private String name;
    private String logoUrl;
    private String description;

    public AdminCategoryRequest(String name, String logoUrl, String description) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    public Category toCategory() {
        return Category.builder()
                .name(name)
                .isDeleted(false)
                .logoUrl(logoUrl)
                .description(description)
                .build();
    }
}
