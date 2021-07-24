package botobo.core.quiz.exception;

import botobo.core.common.exception.BadRequestException;

public class SearchKeywordCreationFailureException extends BadRequestException {
    public SearchKeywordCreationFailureException(String message) {
        super(message);
    }
}
