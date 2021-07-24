package botobo.core.quiz.domain.workbook;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class WorkbookCriteria {

    private SearchKeyword searchKeyword;
    private boolean opened;

    private WorkbookCriteria(SearchKeyword searchKeyword, boolean opened) {
        this.searchKeyword = searchKeyword;
        this.opened = opened;
    }

    @Builder
    public WorkbookCriteria(String keyword, String opened) {
        this(SearchKeyword.from(keyword), applyOpenedRule(opened));
    }

    private static boolean applyOpenedRule(String opened) {
        if (Objects.isNull(opened) || !opened.equalsIgnoreCase("false")) {
            return true;
        }
        return false;
    }

    public boolean isNoSearchKeyword() {
        return searchKeyword.isNoKeyword();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkbookCriteria that = (WorkbookCriteria) o;
        return opened == that.opened && Objects.equals(searchKeyword, that.searchKeyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchKeyword, opened);
    }
}
