package botobo.core.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuizRequest {

    @NotEmpty(message = "퀴즈를 진행하려면 문제집 아이디가 필요합니다.")
    private List<Long> workbookIds;

    public QuizRequest(List<Long> workbookIds) {
        this.workbookIds = workbookIds;
    }
}
