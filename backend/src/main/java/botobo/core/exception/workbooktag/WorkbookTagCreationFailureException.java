package botobo.core.exception.workbooktag;

import botobo.core.exception.BadRequestException;

public class WorkbookTagCreationFailureException extends BadRequestException {
    public WorkbookTagCreationFailureException(String message) {
        super(message);
    }
}
