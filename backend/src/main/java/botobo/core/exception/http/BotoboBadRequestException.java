package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import org.springframework.http.HttpStatus;

public class BotoboBadRequestException extends BotoboException {
    public BotoboBadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
