package botobo.core.exception.heart;


import botobo.core.exception.common.BadRequestException;

public class HeartsCreationFailureException extends BadRequestException {
    public HeartsCreationFailureException(String reason) {
        super(String.format("Hearts 생성에 실패했습니다. (%s)", reason));
    }
}
