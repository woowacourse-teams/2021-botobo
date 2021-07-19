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

    @Builder
    private AdminCategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AdminCategoryResponse of(Category category) {
        return new AdminCategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
