package botobo.core.admin.dto;

import botobo.core.quiz.domain.card.Card;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminCardResponse {
    private Long id;
    private String question;
    private String answer;
    private Long workbookId;
    private int encounterCount;

    @Builder
    private AdminCardResponse(Long id, String question, String answer, Long workbookId, int encounterCount) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.workbookId = workbookId;
        this.encounterCount = encounterCount;
    }

    public static AdminCardResponse of(Card card) {
        return new AdminCardResponse(
                card.getId(),
                card.getQuestion(),
                card.getAnswer(),
                card.getWorkbook().getId(),
                card.getEncounterCount());
    }
}
