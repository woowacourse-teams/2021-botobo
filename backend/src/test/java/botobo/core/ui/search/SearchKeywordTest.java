package botobo.core.ui.search;

import botobo.core.exception.search.SearchKeywordCreationFailureException;
import botobo.core.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static botobo.core.utils.TestUtils.stringGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SearchKeywordTest {

    @Test
    @DisplayName("SearchKeyword 객체 생성 - 성공")
    void create() {
        // given
        final int keywordMaxLength = 30;
        String keyword = stringGenerator(keywordMaxLength);

        // when
        SearchKeyword searchKeyword = SearchKeyword.of(keyword);

        // then
        assertThat(searchKeyword).isEqualTo(
                SearchKeyword.of(keyword)
        );
    }

    @Test
    @DisplayName("탭문자나 개행문자가 있으면 공백으로 변경하여 생성한다.")
    void createWithTabOrNewlineCharacter() {
        // given
        final SearchKeyword expected = SearchKeyword.of(" j a v   a  ");

        // when, then
        assertThat(SearchKeyword.of("\tj\ta\nv\n\n\na\n\t")).isEqualTo(expected);
    }

    @Test
    @DisplayName("SearchKeyword 객체 생성 - 실패, null 입력")
    void createWithNoKeyword() {
        // when, then
        assertThatThrownBy(() -> SearchKeyword.of(null))
                .isInstanceOf(SearchKeywordCreationFailureException.class)
                .hasMessageContaining("검색어는 null일 수 없습니다.");
    }

    @Test
    @DisplayName("SearchKeyword 객체 생성 - 실패, 긴 문자열")
    void createWithLongString() {
        // when, then
        assertThatThrownBy(() -> SearchKeyword.of(TestUtils.stringGenerator(31)))
                .isInstanceOf(SearchKeywordCreationFailureException.class)
                .hasMessageContaining("검색어는 30자 이하여야 합니다.");
    }

    @Test
    @DisplayName("SearchKeyword 객체 생성 - 실패, 짧은 문자열")
    void createWithShortString() {
        // when, then
        assertThatThrownBy(() -> SearchKeyword.of(""))
                .isInstanceOf(SearchKeywordCreationFailureException.class)
                .hasMessageContaining("검색어는 1자 이상이어야 합니다.");
    }

    @Test
    @DisplayName("SearchKeyword 객체 생성 - 실패, 금지어 포함")
    void createWithForbiddenString() {
        // when, then
        assertThatThrownBy(() -> SearchKeyword.of("바보"))
                .isInstanceOf(SearchKeywordCreationFailureException.class)
                .hasMessageContaining("금지어를 입력했습니다");
    }
}