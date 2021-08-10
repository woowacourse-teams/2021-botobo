package botobo.core.exception.heart;


import botobo.core.exception.http.BotoboInternalServerErrorException;

public class HeartCreationFailureException extends BotoboInternalServerErrorException {
    public HeartCreationFailureException(String reason) {
        super(String.format("Heart 생성에 실패했습니다. (%s)", reason));
    }
}
