package botobo.core.ui.search;

import botobo.core.domain.workbook.Workbook;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Objects;

public enum SearchOrder {

    ASC("asc"),
    DESC("desc");

    private String value;

    SearchOrder(String value) {
        this.value = value;
    }

    public static SearchOrder of(String value) {
        if (Objects.isNull(value)) {
            return DESC;
        }
        return Arrays.stream(values())
                .filter(searchOrder -> searchOrder.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(ASC);
    }
}
