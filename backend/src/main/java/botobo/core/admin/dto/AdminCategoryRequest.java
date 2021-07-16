package botobo.core.admin.dto;

import botobo.core.quiz.domain.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AdminCategoryRequest {

    @NotBlank(message = "카테고리명은 필수 입력값입니다.")
    @Length(max = 30, message = "카테고리명은 최소 1글자, 최대 30글자만 가능합니다.")
    private String name;

    public AdminCategoryRequest(String name) {
        this.name = name;
    }

    public Category toCategory() {
        return Category.builder()
                .name(name)
                .isDeleted(false)
                .build();
    }
}
