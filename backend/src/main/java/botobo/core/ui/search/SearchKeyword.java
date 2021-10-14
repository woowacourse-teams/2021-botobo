package botobo.core.ui.search;

import botobo.core.exception.search.ForbiddenSearchKeywordException;
import botobo.core.exception.search.LongSearchKeywordException;
import botobo.core.exception.search.SearchKeywordNullException;
import botobo.core.exception.search.ShortSearchKeywordException;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode
public class SearchKeyword {

    private static final String BLANK_KEYWORD = "";
    private static final int KEYWORD_MIN_LENGTH = 1;
    private static final int KEYWORD_MAX_LENGTH = 30;

    private final String value;

    public static SearchKeyword of(String value) {
        validateNonNull(value);
        String refinedValue = refineValue(value);
        validateLength(refinedValue);
        validateNotForbidden(refinedValue);
        return new SearchKeyword(refinedValue);
    }

    public static SearchKeyword ofAll() {
        return new SearchKeyword(BLANK_KEYWORD);
    }

    private static void validateNonNull(String value) {
        if (Objects.isNull(value)) {
            throw new SearchKeywordNullException();
        }
    }

    private static String refineValue(String value) {
        return value.replaceAll("(\\t|\r\n|\r|\n|\n\r)", " ");
    }

    private static void validateLength(String value) {
        if (value.length() > KEYWORD_MAX_LENGTH) {
            throw new LongSearchKeywordException();
        }
        if (value.length() < KEYWORD_MIN_LENGTH) {
            throw new ShortSearchKeywordException();
        }
    }

    private static void validateNotForbidden(String value) {
        if (value.contains("바보")) {
            throw new ForbiddenSearchKeywordException();
        }
    }

    private SearchKeyword(String value) {
        this.value = value;
    }

    public String getValue() {
        return value.toLowerCase();
    }
}
