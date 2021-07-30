package botobo.core.exception.tag;

import botobo.core.exception.BadRequestException;

public class TagCreationFailureException extends BadRequestException {
    public TagCreationFailureException(String message) {
        super(message);
    }
}
