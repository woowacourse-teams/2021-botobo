package botobo.core.ui.search;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum SearchType {
    NAME("name"),
    TAG("tag"),
    USER("user");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }

    public static SearchType of(String value) {
        if (Objects.isNull(value)) {
            return NAME;
        }
        return Arrays.stream(values())
                .filter(searchType -> searchType.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(InvalidSearchTypeException::new);
    }
}
