package botobo.core.admin.dto;

import botobo.core.quiz.domain.answer.Answer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminAnswerResponse {
    private Long id;
    private String content;
    private Long cardId;

    @Builder
    private AdminAnswerResponse(Long id, String content, Long cardId) {
        this.id = id;
        this.content = content;
        this.cardId = cardId;
    }

    public static AdminAnswerResponse of(Answer answer) {
        return new AdminAnswerResponse(
                answer.getId(),
                answer.getContent(),
                answer.getCard().getId());
    }
}
