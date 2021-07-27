package botobo.core.domain.workbook.criteria;

import botobo.core.exception.workbook.WorkbookSearchFailureException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@EqualsAndHashCode
@Getter
public class SearchKeyword {

    private static final int KEYWORD_MAX_LENGTH = 30;
    private static final String NO_KEYWORD_VALUE = "IMPOSSIBLE   KEYWORD";

    private static final SearchKeyword NO_SEARCH_KEYWORD = new SearchKeyword(NO_KEYWORD_VALUE);

    private final String value;

    private SearchKeyword(String value) {
        String refinedValue = refineValue(value);
        validateLength(refinedValue);
        validateNotForbidden(refinedValue);
        this.value = refinedValue;
    }

    private String refineValue(String value) {
        return value.trim()
                .replaceAll("(\\t|\r\n|\r|\n|\n\r)", " ")
                .replaceAll("[ ]+", " ");
    }

    private void validateLength(String value) {
        if (value.length() > KEYWORD_MAX_LENGTH) {
            throw new WorkbookSearchFailureException(
                    String.format("검색어는 %d자 이하여야 합니다.", KEYWORD_MAX_LENGTH)
            );
        }
    }

    private void validateNotForbidden(String value) {
        if (value.contains("바보")) {
            throw new WorkbookSearchFailureException(
                    String.format("금지어를 입력했습니다. (%s)", value)
            );
        }
    }

    public static SearchKeyword from(String value) {
        if (Objects.isNull(value)) {
            return NO_SEARCH_KEYWORD;
        }
        return new SearchKeyword(value);
    }

    public boolean isNoKeyword() {
        return this == NO_SEARCH_KEYWORD;
    }
}
