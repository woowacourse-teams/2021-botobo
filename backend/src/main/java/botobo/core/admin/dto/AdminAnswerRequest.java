package botobo.core.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminAnswerRequest {
    private String content;
    private Long cardId;

    public AdminAnswerRequest(String content, Long cardId) {
        this.content = content;
        this.cardId = cardId;
    }
}
