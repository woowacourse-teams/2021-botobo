package botobo.core.exception.search;


import botobo.core.exception.common.BadRequestException;

public class InvalidSearchOrderException extends BadRequestException {
    public InvalidSearchOrderException() {
        super("유효하지 않은 정렬 방향입니다. 유효한 정렬 방식 : ASC, DESC");
    }
}
