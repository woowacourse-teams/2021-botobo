package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BotoboException {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
