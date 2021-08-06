package botobo.core.exception.search;

import botobo.core.exception.BadRequestException;

public class InvalidSearchCriteriaException extends BadRequestException {
    public InvalidSearchCriteriaException() {
        super("유효하지 않은 정렬 조건입니다. 유효한 정렬 조건 : date, name, count, like");
    }
}
