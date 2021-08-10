package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class BotoboNotFoundException extends BotoboException {
    public BotoboNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
