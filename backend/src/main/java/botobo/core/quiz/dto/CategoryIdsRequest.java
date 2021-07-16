package botobo.core.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryIdsRequest {

    @NotEmpty(message = "퀴즈를 진행하려면 카테고리 아이디가 필요합니다.")
    private List<Long> categoryIds;

    public CategoryIdsRequest(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
