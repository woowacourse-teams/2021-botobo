package botobo.core.ui.search;

import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.search.InvalidSearchCriteriaException;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
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

    public Expression<?> toExpression(CriteriaBuilder builder, Root<Workbook> root) {
        if (DATE.equals(this)) {
            return root.get("createdAt");
        }
        if (NAME.equals(this)) {
            return root.get("name");
        }
        if (COUNT.equals(this)) {
            return builder.size(root.get("cards").get("cards"));
        }
        return builder.size(root.get("hearts").get("hearts"));
    }
}
