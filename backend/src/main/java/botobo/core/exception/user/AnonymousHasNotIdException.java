package botobo.core.exception.user;

import botobo.core.exception.http.InternalServerErrorException;

public class AnonymousHasNotIdException extends InternalServerErrorException {
    public AnonymousHasNotIdException(String message) {
        super(message);
    }
}
