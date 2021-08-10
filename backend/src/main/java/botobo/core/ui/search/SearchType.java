package botobo.core.ui.search;

import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.search.InvalidSearchTypeException;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

@Getter
public enum SearchType {
    NAME(
            "name",
            root -> root.get("name"),
            keyword -> "%" + keyword.toLowerCase() + "%"
    ),
    TAG(
            "tag",
            root -> root.join("workbookTags").get("tag").get("tagName").get("value"),
            String::toLowerCase
    ),
    USER(
            "user",
            root -> root.get("user").get("userName"),
            keyword -> keyword
    );

    private final String value;
    private final Function<Root<Workbook>, Expression<String>> target;
    private final Function<String, String> pattern;

    SearchType(String value, Function<Root<Workbook>, Expression<String>> target, Function<String, String> pattern) {
        this.value = value;
        this.target = target;
        this.pattern = pattern;
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

    public Expression<String> toExpression(Root<Workbook> root, CriteriaBuilder builder) {
        if (this.equals(NAME)) {
            return builder.lower(target.apply(root));
        }
        return target.apply(root);
    }

    public String toPattern(String value) {
        return pattern.apply(value);
    }
}
