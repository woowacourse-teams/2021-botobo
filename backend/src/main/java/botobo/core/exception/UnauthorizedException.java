package botobo.core.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
