package botobo.core.quiz.exception;

import botobo.core.common.exception.NotFoundException;

public class WorkbookNotFoundException extends NotFoundException {
    public WorkbookNotFoundException() {
        super("해당 문제집을 찾을 수 없습니다.");
    }

    public WorkbookNotFoundException(String message) {
        super(message);
    }
}
