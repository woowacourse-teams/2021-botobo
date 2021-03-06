package botobo.core.dto.card;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.Cards;
import botobo.core.domain.workbook.Workbook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizResponse {

    private Long id;
    private String question;
    private String answer;
    private String workbookName;
    private int encounterCount;

    private static QuizResponse of(Card card) {
        final Workbook workbook = card.getWorkbook();
        final String answer = card.getAnswer();
        return QuizResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(answer)
                .workbookName(workbook.getName())
                .encounterCount(card.getEncounterCount())
                .build();
    }

    public static List<QuizResponse> listOf(List<Card> cards) {
        return cards.stream()
                .map(QuizResponse::of)
                .collect(Collectors.toList());
    }

    public static List<QuizResponse> cardsOf(Cards nextCards) {
        return listOf(nextCards.getCards());
    }
}
