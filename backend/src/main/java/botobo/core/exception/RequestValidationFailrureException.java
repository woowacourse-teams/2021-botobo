package botobo.core.exception;

import org.springframework.http.HttpStatus;

public class RequestValidationFailrureException extends BotoboException {
    public RequestValidationFailrureException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
