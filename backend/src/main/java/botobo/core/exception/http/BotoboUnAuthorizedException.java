package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class BotoboUnAuthorizedException extends BotoboException {
    public BotoboUnAuthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }
}
