package botobo.core.exception.common;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException() {
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
