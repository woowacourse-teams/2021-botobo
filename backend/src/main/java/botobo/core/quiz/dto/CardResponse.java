package botobo.core.quiz.dto;

import botobo.core.quiz.domain.card.Card;
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
public class CardResponse {

    private Long id;
    private String question;
    private String answer;

    public static CardResponse of(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .build();
    }

    public static List<CardResponse> listOf(List<Card> cards) {
        return cards.stream()
                .map(CardResponse::of)
                .collect(Collectors.toList());
    }
}
