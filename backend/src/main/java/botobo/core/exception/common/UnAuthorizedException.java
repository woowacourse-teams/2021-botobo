package botobo.core.exception.common;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
    }

    public UnAuthorizedException(String message) {
        super(message);
    }
}
