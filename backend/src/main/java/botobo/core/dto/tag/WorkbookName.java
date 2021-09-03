package botobo.core.dto.tag;

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
        if (workbook.isBlank() || workbook.length() > MAX_NAME_LENGTH) {
            return EMPTY;
        }
        return workbook;
    }

    public boolean isEmpty() {
        return workbook.isEmpty();
    }
}
