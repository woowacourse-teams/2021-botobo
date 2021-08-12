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

    @NotEmpty(message = "Q001")
    private List<Long> workbookIds;

    @Range(min = 10, max = 30, message = "Q002")
    private int count;

    @Builder
    public QuizRequest(List<Long> workbookIds, int count) {
        this.workbookIds = workbookIds;
        this.count = count;
    }
}
