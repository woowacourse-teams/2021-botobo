package botobo.core.ui.search;

import botobo.core.exception.search.InvalidPageableException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookSearchParameter {

    private static final int MINIMUM_PAGE_SIZE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MINIMUM_START_PAGE = 0;
    private static final int DEFAULT_START_PAGE = 0;

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
            if (value < MINIMUM_PAGE_SIZE) {
                throw new InvalidPageableException("페이지의 크기는 0보다 커야합니다.");
            }
            return value;
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_SIZE;
        }
    }
}
