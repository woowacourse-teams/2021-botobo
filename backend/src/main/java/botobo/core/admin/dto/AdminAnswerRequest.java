package botobo.core.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AdminAnswerRequest {

    @NotBlank(message = "답변 필수 입력값 입니다.")
    private String content;

    @NotNull(message = "카드의 Id는 필수 입력값 입니다.")
    private Long cardId;

    public AdminAnswerRequest(String content, Long cardId) {
        this.content = content;
        this.cardId = cardId;
    }
}
