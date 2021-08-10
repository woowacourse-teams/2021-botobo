package botobo.core.exception;

import botobo.core.exception.http.BotoboInternalServerErrorException;

public class UndefinedException extends BotoboInternalServerErrorException {
    public UndefinedException(String serverMessage) {
        super(serverMessage);
    }
}
