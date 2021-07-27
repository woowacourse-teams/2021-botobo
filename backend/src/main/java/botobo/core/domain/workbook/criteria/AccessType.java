package botobo.core.domain.workbook.criteria;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum AccessType {
    ALL("all"), PUBLIC("public"), PRIVATE("private");

    private final String value;

    AccessType(String value) {
        this.value = value;
    }

    public static AccessType from(String value) {
        if (Objects.isNull(value)) {
            return PUBLIC;
        }
        return Arrays.stream(values())
                .filter(accessType -> value.equalsIgnoreCase(accessType.value))
                .findAny()
                .orElse(PUBLIC);
    }

    public boolean isAll() {
        return this.equals(ALL);
    }

    public boolean isPublic() {
        return this.equals(PUBLIC);
    }

    public boolean isPrivate() {
        return this.equals(PRIVATE);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
