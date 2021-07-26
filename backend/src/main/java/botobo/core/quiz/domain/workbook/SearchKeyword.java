package botobo.core.quiz.domain.workbook;

import botobo.core.quiz.exception.WorkbookSearchFailureException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@EqualsAndHashCode
@Getter
public class SearchKeyword {

    private static final int KEYWORD_MAX_LENGTH = 30;
    private static final String NO_KEYWORD_VALUE = "";

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

    // TODO: 검토 후 개선
    private void validateNotForbidden(String value) {
        if (value.contains("바보")) {
            throw new WorkbookSearchFailureException(
                    String.format("금지어를 입력했습니다. (%s)", value)
            );
        }
    }

    public static SearchKeyword from(String value) {
        if (isNoKeywordValue(value)) {
            return NO_SEARCH_KEYWORD;
        }
        return new SearchKeyword(value);
    }

    private static boolean isNoKeywordValue(String value) {
        return Objects.isNull(value) || value.isBlank();
    }

    public boolean isNoKeyword() {
        return this == NO_SEARCH_KEYWORD;
    }
}