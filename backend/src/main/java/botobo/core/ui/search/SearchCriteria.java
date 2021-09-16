package botobo.core.ui.search;

import botobo.core.exception.search.InvalidSearchCriteriaException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum SearchCriteria {
    DATE("date"),
    NAME("name"),
    COUNT("count"),
    HEART("heart");

    private String value;

    SearchCriteria(String value) {
        this.value = value;
    }

    public static SearchCriteria of(String value) {
        if (Objects.isNull(value)) {
            return DATE;
        }
        return Arrays.stream(values())
                .filter(searchCriteria -> searchCriteria.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(InvalidSearchCriteriaException::new);
    }
}
