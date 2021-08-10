package botobo.core.exception.http;

import botobo.core.exception.BotoboException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BotoboInternalServerErrorException extends BotoboException {

    private final String serverMessage;

    public BotoboInternalServerErrorException(String serverMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
        this.serverMessage = serverMessage;
    }
}
