package botobo.core.card.exception;

import botobo.core.exception.NotFoundException;

public class CardNotFoundException extends NotFoundException {
    public CardNotFoundException() {
        super("해당 카드를 찾을 수 없습니다.");
    }

    public CardNotFoundException(String message) {
        super(message);
    }
}
