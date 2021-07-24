package botobo.core.quiz.domain.card;

import botobo.core.quiz.utils.CardFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GuestCards {
    private static final Cards cards = new Cards(CardFactory.createGuestCards());

    public static Cards getInstance() {
        return cards;
    }
}
