package botobo.core.exception.common;

import botobo.core.exception.http.InternalServerErrorException;

public class UndefinedException extends InternalServerErrorException {
    public UndefinedException(String serverMessage) {
        super(serverMessage);
    }
}
