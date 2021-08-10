package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class BotoboForbiddenException extends BotoboException {
    public BotoboForbiddenException() {
        super(HttpStatus.FORBIDDEN);
    }
}
