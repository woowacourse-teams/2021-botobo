package botobo.core.ui.search;

import botobo.core.exception.search.InvalidPageSizeException;
import botobo.core.exception.search.InvalidPageStartException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    private SearchKeyword searchKeyword;
    private SearchCriteria searchCriteria;
    private int start;
    private int size;

    private WorkbookSearchParameter(SearchCriteria searchCriteria, SearchKeyword searchKeyword, int start, int size) {
        this.start = start;
        this.size = size;
        this.searchKeyword = searchKeyword;
        this.searchCriteria = searchCriteria;
    }

    public static WorkbookSearchParameter ofRequest(String searchCriteria,
                                                    String searchKeyword,
                                                    String start, String size) {
        return of(
                SearchCriteria.of(searchCriteria),
                SearchKeyword.of(searchKeyword),
                initializeStartValue(start),
                initializeSizeValue(size)
        );
    }

    public static WorkbookSearchParameter of(SearchCriteria searchCriteria,
                                             SearchKeyword searchKeyword,
                                             int start, int size) {
        return new WorkbookSearchParameter(
                searchCriteria,
                searchKeyword,
                start,
                size
        );
    }

    private static int initializeStartValue(String start) {
        try {
            int value = Integer.parseInt(start);
            if (value < MINIMUM_START_PAGE) {
                throw new InvalidPageStartException();
            }
            return value;
        } catch (NumberFormatException e) {
            return DEFAULT_START_PAGE;
        }
    }

    private static int initializeSizeValue(String size) {
        try {
            int value = Integer.parseInt(size);
            if (value < MINIMUM_PAGE_SIZE || value > MAXIMUM_PAGE_SIZE) {
                throw new InvalidPageSizeException();
            }
            return value;
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_SIZE;
        }
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(start, size);
    }
}
