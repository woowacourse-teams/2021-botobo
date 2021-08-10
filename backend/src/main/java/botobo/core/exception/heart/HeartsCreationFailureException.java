package botobo.core.exception.heart;


import botobo.core.exception.http.BotoboInternalServerErrorException;

public class HeartsCreationFailureException extends BotoboInternalServerErrorException {
    public HeartsCreationFailureException(String reason) {
        super(String.format("Hearts 생성에 실패했습니다. (%s)", reason));
    }
}
