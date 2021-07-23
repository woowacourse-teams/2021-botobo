package botobo.core.quiz.dto;

import botobo.core.quiz.domain.card.Card;
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
public class CardSimpleResponse {

    private Long id;
    private String question;
    private String answer;
    private boolean bookmark;
    private int encounterCount;

    public static CardSimpleResponse of(Card card) {
        return CardSimpleResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .bookmark(card.isBookmark())
                .encounterCount(card.getEncounterCount())
                .build();
    }

    public static List<CardSimpleResponse> listOf(List<Card> cards) {
        return cards.stream()
                .map(CardSimpleResponse::of)
                .collect(Collectors.toList());
    }
}
