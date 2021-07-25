package botobo.core.quiz.dto;

import botobo.core.quiz.domain.card.Card;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class CardUpdateResponse {

    private Long id;
    private String question;
    private String answer;
    private Long workbookId;
    private int encounterCount;
    private boolean bookmark;
    private boolean nextQuiz;

    public static CardUpdateResponse of(Card card) {
        return CardUpdateResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .workbookId(card.getWorkbook().getId())
                .encounterCount(card.getEncounterCount())
                .bookmark(card.isBookmark())
                .nextQuiz(card.isNextQuiz())
                .build();
    }
}
