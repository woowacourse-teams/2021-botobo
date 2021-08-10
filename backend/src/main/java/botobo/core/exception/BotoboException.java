package botobo.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BotoboException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ErrorType errorType = ErrorType.of(this.getClass());
}
