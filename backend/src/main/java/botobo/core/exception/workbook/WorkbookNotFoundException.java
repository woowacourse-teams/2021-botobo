package botobo.core.exception.workbook;

import botobo.core.exception.NotFoundException;

public class WorkbookNotFoundException extends NotFoundException {
    public WorkbookNotFoundException() {
        super("해당 문제집을 찾을 수 없습니다.");
    }

    public WorkbookNotFoundException(String message) {
        super(message);
    }
}
