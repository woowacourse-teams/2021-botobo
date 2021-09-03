package botobo.core.dto.tag;

import botobo.core.exception.workbook.WorkbookNameLengthException;
import lombok.Getter;

@Getter
public class WorkbookName {
    private static final int MAX_NAME_LENGTH = 30;
    private static final String EMPTY = "";

    private final String workbook;

    public WorkbookName(String workbook) {
        this.workbook = validateName(workbook);
    }

    private String validateName(String workbook) {
        if (workbook.isBlank()) {
            return EMPTY;
        }
        if (workbook.length() > MAX_NAME_LENGTH) {
            throw new WorkbookNameLengthException();
        }
        return workbook;
    }

    public boolean isEmpty() {
        return workbook.isEmpty();
    }
}
