package botobo.core.exception.heart;


import botobo.core.exception.http.InternalServerErrorException;

public class HeartRemovalFailureException extends InternalServerErrorException {
    public HeartRemovalFailureException() {
        super("제거할 하트를 찾지 못했습니다.");
    }
}
