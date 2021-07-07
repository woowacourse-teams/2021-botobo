package botobo.core.category.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryIdsRequest {
    private List<Long> categoryIds;

    public CategoryIdsRequest(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
