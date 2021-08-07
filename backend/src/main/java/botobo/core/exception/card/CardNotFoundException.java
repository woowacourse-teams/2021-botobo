package botobo.core.exception.card;

import botobo.core.exception.common.NotFoundException;

public class CardNotFoundException extends NotFoundException {
    public CardNotFoundException() {
        super("해당 카드를 찾을 수 없습니다.");
    }

    public CardNotFoundException(String message) {
        super(message);
    }
}
