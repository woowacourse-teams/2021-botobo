package botobo.core.ui.search;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum SearchCriteria {
    DATE("date"),
    NAME("name"),
    COUNT("count"),
    LIKE("like");

    private String name;

    SearchCriteria(String name) {
        this.name = name;
    }

    public static SearchCriteria of(String value) {
        if (Objects.isNull(value)) {
            return DATE;
        }
        return Arrays.stream(values())
                .filter(searchCriteria -> searchCriteria.name.equalsIgnoreCase(value))
                .findFirst()
                .orElse(DATE);
    }
}
