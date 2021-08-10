package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BotoboException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN);
    }
}
