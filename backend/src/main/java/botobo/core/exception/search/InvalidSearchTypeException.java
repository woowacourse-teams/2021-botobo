package botobo.core.exception.search;

import botobo.core.exception.BadRequestException;

public class InvalidSearchTypeException extends BadRequestException {
    public InvalidSearchTypeException() {
        super("유효하지 않은 검색 타입입니다. 유효한 검색 타임 : name, tag, user");
    }
}
