package botobo.core.dto.admin;

import botobo.core.domain.card.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminCardResponse {
    private Long id;
    private String question;
    private String answer;
    private Long workbookId;
    private int encounterCount;

    public static AdminCardResponse of(Card card) {
        return AdminCardResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .workbookId(card.getWorkbook().getId())
                .encounterCount(card.getEncounterCount())
                .build();
    }
}
