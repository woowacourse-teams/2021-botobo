package botobo.core.dto.tag;

import botobo.core.exception.workbook.WorkbookNameLengthException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class FilterCriteria {
    private static final int MAX_NAME_LENGTH = 30;
    private static final String EMPTY = "";

    private final String workbook;

    public FilterCriteria(String workbook) {
        this.workbook = validateName(workbook);
    }

    private String validateName(String workbook) {
        if (Objects.isNull(workbook) || workbook.isEmpty()) {
            return EMPTY;
        }
        if (workbook.length() > MAX_NAME_LENGTH) {
            throw new WorkbookNameLengthException();
        }
        return workbook.toLowerCase();
    }

    public boolean isEmpty() {
        return workbook.isEmpty();
    }
}
