package botobo.core.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NextQuizCardsRequest {

    @NotNull(message = "유효하지 않은 또 보기 카드 등록 요청입니다.")
    private List<Long> cardIds;
}
