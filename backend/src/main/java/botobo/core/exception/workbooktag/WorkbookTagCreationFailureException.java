package botobo.core.exception.workbooktag;

import botobo.core.exception.http.InternalServerErrorException;

public class WorkbookTagCreationFailureException extends InternalServerErrorException {
    public WorkbookTagCreationFailureException(String message) {
        super(message);
    }
}
