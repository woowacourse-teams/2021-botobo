package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BotoboException {
    public ConflictException() {
        super(HttpStatus.CONFLICT);
    }
}
