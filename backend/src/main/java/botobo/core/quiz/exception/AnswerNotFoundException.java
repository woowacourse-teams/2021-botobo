package botobo.core.quiz.exception;

import botobo.core.exception.NotFoundException;

public class AnswerNotFoundException extends NotFoundException {
    public AnswerNotFoundException() {
        super("답변을 찾을 수 없습니다.");
    }

    public AnswerNotFoundException(String message) {
        super(message);
    }
}
