package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends BotoboException {
    public UnAuthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }
}
