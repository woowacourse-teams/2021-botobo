package botobo.core.exception.workbook;

import botobo.core.exception.common.BadRequestException;

public class SearchKeywordCreationFailureException extends BadRequestException {
    public SearchKeywordCreationFailureException(String message) {
        super(message);
    }
}
