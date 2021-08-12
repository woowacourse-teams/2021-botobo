package botobo.core.ui.search;

import botobo.core.exception.search.ForbiddenSearchKeywordException;
import botobo.core.exception.search.LongSearchKeywordException;
import botobo.core.exception.search.SearchKeywordNullException;
import botobo.core.exception.search.ShortSearchKeywordException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@EqualsAndHashCode
@Getter
public class SearchKeyword {

    private static final int KEYWORD_MIN_LENGTH = 1;
    private static final int KEYWORD_MAX_LENGTH = 30;

    private final String value;

    private SearchKeyword(String value) {
        validateNonNull(value);
        String refinedValue = refineValue(value);
        validateLength(refinedValue);
        validateNotForbidden(refinedValue);
        this.value = refinedValue;
    }

    private void validateNonNull(String value) {
        if (Objects.isNull(value)) {
            throw new SearchKeywordNullException();
        }
    }

    private String refineValue(String value) {
        return value.replaceAll("(\\t|\r\n|\r|\n|\n\r)", " ");
    }

    private void validateLength(String value) {
        if (value.length() > KEYWORD_MAX_LENGTH) {
            throw new LongSearchKeywordException();
        }
        if (value.length() < KEYWORD_MIN_LENGTH) {
            throw new ShortSearchKeywordException();
        }
    }

    private void validateNotForbidden(String value) {
        if (value.contains("바보")) {
            throw new ForbiddenSearchKeywordException();
        }
    }

    public static SearchKeyword of(String value) {
        return new SearchKeyword(value);
    }

    public String toLowercase() {
        return value.toLowerCase();
    }
}
