package botobo.core.exception.common;

public class ConflictException extends RuntimeException{
    public ConflictException() {
        super();
    }

    public ConflictException(String message) {
        super(message);
    }
}
