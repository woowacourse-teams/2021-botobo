package botobo.core.exception.search;


import botobo.core.exception.common.BadRequestException;

public class InvalidPageableException extends BadRequestException {
    public InvalidPageableException(String message) {
        super(message);
    }
}
