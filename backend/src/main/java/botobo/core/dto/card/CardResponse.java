package botobo.core.dto.card;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.Cards;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class CardResponse {

    private Long id;
    private String question;
    private String answer;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long workbookId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer encounterCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean bookmark;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean nextQuiz;

    public static List<CardResponse> listOf(Cards cards) {
        return cards.stream()
                .map(CardResponse::of)
                .collect(Collectors.toList());
    }

    public static CardResponse of(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .workbookId(card.getWorkbook().getId())
                .encounterCount(card.getEncounterCount())
                .bookmark(card.isBookmark())
                .nextQuiz(card.isNextQuiz())
                .build();
    }

    public static List<CardResponse> listOfSimple(Cards cards) {
        return cards.stream()
                .map(CardResponse::ofSimple)
                .collect(Collectors.toList());
    }

    public static CardResponse ofSimple(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .build();
    }
}
