package botobo.core.exception.workbooktag;

import botobo.core.exception.common.BadRequestException;

public class WorkbookTagCreationFailureException extends BadRequestException {
    public WorkbookTagCreationFailureException(String message) {
        super(message);
    }
}
