package botobo.core.ui.search;

import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.search.InvalidPageableException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookSearchParameter {

    private static final int MINIMUM_START_PAGE = 0;
    private static final int DEFAULT_START_PAGE = 0;
    private static final int MINIMUM_PAGE_SIZE = 1;
    private static final int MAXIMUM_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private SearchType searchType;
    private SearchKeyword searchKeyword;
    private SearchCriteria searchCriteria;
    private SearchOrder searchOrder;
    private int start;
    private int size;

    @Builder
    private WorkbookSearchParameter(String searchType, String searchCriteria, String searchOrder, String searchKeyword, String start, String size) {
        this.start = initializeStartValue(start);
        this.size = initializeSizeValue(size);
        this.searchType = SearchType.of(searchType);
        this.searchKeyword = SearchKeyword.of(searchKeyword);
        this.searchCriteria = SearchCriteria.of(searchCriteria);
        this.searchOrder = SearchOrder.of(searchOrder);
    }

    private int initializeStartValue(String start) {
        try {
            int value = Integer.parseInt(start);
            if (value < MINIMUM_START_PAGE) {
                throw new InvalidPageableException("페이지의 시작 값은 음수가 될 수 없습니다.");
            }
            return value;
        } catch (NumberFormatException e) {
            return DEFAULT_START_PAGE;
        }
    }

    private int initializeSizeValue(String size) {
        try {
            int value = Integer.parseInt(size);
            if (value < MINIMUM_PAGE_SIZE || value > MAXIMUM_PAGE_SIZE) {
                throw new InvalidPageableException("유효하지 않은 페이지 크기입니다. 유효한 크기 : 1 ~ 100");
            }
            return value;
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_SIZE;
        }
    }

    public Specification<Workbook> toSpecification() {
        Specification<Workbook> first = (root, query, builder) ->
                builder.greaterThan(builder.size(root.get("cards").get("cards")), 0);
        Specification<Workbook> second = (root, query, builder) -> {
            Order order = toOrder(root, builder);
            query.orderBy(order);
            return builder.like(toTargetObject(root, builder), toPattern());
        };
        return Specification.where(first).and(second);
    }

    public Order toOrder(Root<Workbook> root, CriteriaBuilder builder) {
        return searchOrder.toOrder(builder, root, searchCriteria);
    }

    public Expression<String> toTargetObject(Root<Workbook> root, CriteriaBuilder builder) {
        if (SearchType.NAME.equals(this.searchType)) {
            return builder.lower(searchType.getExpression().apply(root));
        }
        return searchType.getExpression().apply(root);
    }

    public String toPattern() {
        return searchType.getPattern().apply(searchKeyword.getValue());
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(start, size);
    }
}
