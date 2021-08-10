package botobo.core.ui.search;

import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.search.InvalidSearchOrderException;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Objects;

@Getter
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
                .orElseThrow(InvalidSearchOrderException::new);
    }

    public Order toOrder(CriteriaBuilder builder, Root<Workbook> root, SearchCriteria searchCriteria) {
        if (this.equals(ASC)) {
            return builder.asc(searchCriteria.toExpression(builder, root));
        }
        return builder.desc(searchCriteria.toExpression(builder, root));
    }
}
