package botobo.core.quiz.exception;

import botobo.core.common.exception.NotFoundException;

public class CardNotFoundException extends NotFoundException {
    public CardNotFoundException() {
        super("해당 카드를 찾을 수 없습니다.");
    }

    public CardNotFoundException(String message) {
        super(message);
    }
}
