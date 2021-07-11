package botobo.core.admin.dto;

import botobo.core.category.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AdminCategoryRequest {
    @NotNull(message = "카테고리명은 필수 입력값입니다.")
    @Length(min = 1, max = 30, message = "카테고리명은 최소 1글자, 최대 30글자만 가능합니다.")
    private String name;

    @NotNull(message = "로고 url은 필수 입력값입니다.")
    @Length(max = 100, message = "logo Url은 최대 100글자까지 가능합니다.")
    private String logoUrl;

    @NotNull(message = "설명은 필수 입력값입니다.")
    @Length(max = 255, message = "설명은 최대 255글자까지 가능합니다.")
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
