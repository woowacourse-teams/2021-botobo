package botobo.core.ui.search;

import botobo.core.exception.search.SearchKeywordCreationFailureException;
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
            throw new SearchKeywordCreationFailureException("검색어는 null일 수 없습니다.");
        }
    }

    private String refineValue(String value) {
        return value.trim()
                .replaceAll("(\\t|\r\n|\r|\n|\n\r)", " ")
                .replaceAll("[ ]+", " ");
    }

    private void validateLength(String value) {
        if (value.length() > KEYWORD_MAX_LENGTH) {
            throw new SearchKeywordCreationFailureException(
                    String.format("검색어는 %d자 이하여야 합니다.", KEYWORD_MAX_LENGTH)
            );
        }
        if (value.length() < KEYWORD_MIN_LENGTH) {
            throw new SearchKeywordCreationFailureException(
                    String.format("검색어는 %d자 이상이어야 합니다.", KEYWORD_MIN_LENGTH)
            );
        }
    }

    private void validateNotForbidden(String value) {
        if (value.contains("바보")) {
            throw new SearchKeywordCreationFailureException(
                    String.format("금지어를 입력했습니다. (%s)", value)
            );
        }
    }

    public static SearchKeyword of(String value) {
        return new SearchKeyword(value);
    }
}
