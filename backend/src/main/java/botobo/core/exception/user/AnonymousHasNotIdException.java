package botobo.core.exception.user;

import botobo.core.exception.http.BotoboInternalServerErrorException;

public class AnonymousHasNotIdException extends BotoboInternalServerErrorException {
    public AnonymousHasNotIdException(String message) {
        super(message);
    }
}
