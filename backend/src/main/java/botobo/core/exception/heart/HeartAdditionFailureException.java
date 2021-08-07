package botobo.core.exception.heart;

import botobo.core.exception.BadRequestException;

public class HeartAdditionFailureException extends BadRequestException {
    public HeartAdditionFailureException() {
        super("하트가 이미 존재하여 추가에 실패했습니다.");
    }
}
