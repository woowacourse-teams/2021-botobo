package botobo.core.exception.workbooktag;

import botobo.core.exception.http.BotoboInternalServerErrorException;

public class WorkbookTagCreationFailureException extends BotoboInternalServerErrorException {
    public WorkbookTagCreationFailureException(String message) {
        super(message);
    }
}
