package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalServerErrorException extends BotoboException {

    private final String serverMessage;

    public InternalServerErrorException(String serverMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
        this.serverMessage = serverMessage;
    }
}
