package botobo.core.exception.heart;

import botobo.core.exception.BadRequestException;

public class HeartRemovalFailureException extends BadRequestException {
    public HeartRemovalFailureException() {
        super("제거할 하트를 찾지 못했습니다.");
    }
}
