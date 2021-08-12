package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BotoboException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
