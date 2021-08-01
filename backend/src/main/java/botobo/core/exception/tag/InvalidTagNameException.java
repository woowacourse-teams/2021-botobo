package botobo.core.exception.tag;

import botobo.core.exception.BadRequestException;

public class InvalidTagNameException extends BadRequestException {
    public InvalidTagNameException(String reason) {
        super(String.format("적절하지 않은 태그입니다. (%s)", reason));
    }
}
