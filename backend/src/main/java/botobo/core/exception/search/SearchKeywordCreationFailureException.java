package botobo.core.exception.search;

import botobo.core.exception.common.BadRequestException;

public class SearchKeywordCreationFailureException extends BadRequestException {
    public SearchKeywordCreationFailureException(String message) {
        super(message);
    }
}
