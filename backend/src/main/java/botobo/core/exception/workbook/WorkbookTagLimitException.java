package botobo.core.exception.workbook;

import botobo.core.exception.BadRequestException;

public class WorkbookTagLimitException extends BadRequestException {

    public WorkbookTagLimitException(int maxTagSize) {
        super(String.format("문제집이 가질 수 있는 태그수는 최대 %s개 입니다.", maxTagSize));
    }
}
