package botobo.core.dto.card;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuizRequest {

    @NotEmpty(message = "퀴즈를 진행하려면 문제집 아이디가 필요합니다.")
    private List<Long> workbookIds;

    @Range(min = 10, max = 30, message = "퀴즈의 개수는 10 ~ 30 사이의 수만 가능합니다.")
    private int count;

    @Builder
    public QuizRequest(List<Long> workbookIds, int count) {
        this.workbookIds = workbookIds;
        this.count = count;
    }
}
