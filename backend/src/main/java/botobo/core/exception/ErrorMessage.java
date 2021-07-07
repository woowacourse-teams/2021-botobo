package botobo.core.exception;

public class ErrorMessage {
    private final String message;

    private ErrorMessage(String message) {
        this.message = message;
    }

    public static ErrorMessage of(String message) {
        return new ErrorMessage(message);
    }
}
