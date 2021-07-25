package botobo.core.quiz.exception;

import botobo.core.common.exception.BadRequestException;

public class WorkbookSearchFailureException extends BadRequestException {
    public WorkbookSearchFailureException(String message) {
        super(message);
    }
}
