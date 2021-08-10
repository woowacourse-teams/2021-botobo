package botobo.core.exception.request;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class RequestValidationFailrureException extends BotoboException {
    public RequestValidationFailrureException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
