package botobo.core.quiz.domain.workbook;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum OwnerType {
    MINE("mine"), ALL("all");

    private final String value;

    OwnerType(String value) {
        this.value = value;
    }

    public static OwnerType from(String value) {
        if (Objects.isNull(value)) {
            return ALL;
        }
        return Arrays.stream(values())
                .filter(ownerType -> value.equalsIgnoreCase(ownerType.value))
                .findAny()
                .orElse(ALL);
    }

    public boolean isMine() {
        return this == MINE;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
