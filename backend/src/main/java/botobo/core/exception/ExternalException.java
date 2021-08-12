package botobo.core.exception;

import org.springframework.http.HttpStatus;

public class ExternalException extends BotoboException {
    public ExternalException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
