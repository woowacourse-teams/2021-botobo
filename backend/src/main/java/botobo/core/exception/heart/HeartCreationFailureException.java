package botobo.core.exception.heart;

import botobo.core.exception.BadRequestException;

public class HeartCreationFailureException extends BadRequestException {
    public HeartCreationFailureException(String reason) {
        super(String.format("Heart 생성에 실패했습니다. (%s)", reason));
    }
}
