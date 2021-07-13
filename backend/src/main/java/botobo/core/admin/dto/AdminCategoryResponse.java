package botobo.core.admin.dto;

import botobo.core.quiz.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminCategoryResponse {

    private Long id;
    private String name;
    private String logoUrl;
    private String description;

    @Builder
    private AdminCategoryResponse(Long id, String name, String logoUrl, String description) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    public static AdminCategoryResponse of(Category category) {
        return new AdminCategoryResponse(
                category.getId(),
                category.getName(),
                category.getLogoUrl(),
                category.getDescription()
        );
    }
}
