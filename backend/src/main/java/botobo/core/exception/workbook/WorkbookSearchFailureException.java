package botobo.core.exception.workbook;

import botobo.core.exception.BadRequestException;

public class WorkbookSearchFailureException extends BadRequestException {
    public WorkbookSearchFailureException(String message) {
        super(message);
    }
}
