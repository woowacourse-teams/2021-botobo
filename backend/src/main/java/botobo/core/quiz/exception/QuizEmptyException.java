package botobo.core.quiz.exception;

import botobo.core.common.exception.BadRequestException;

public class QuizEmptyException extends BadRequestException {
    public QuizEmptyException() {
        super("퀴즈에 문제가 존재하지 않습니다.");
    }

    public QuizEmptyException(String message) {
        super(message);
    }
}
