package botobo.core.quiz.domain.workbook;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class WorkbookCriteria {

    private SearchKeyword searchKeyword;
    private AccessType accessType;

    private WorkbookCriteria(SearchKeyword searchKeyword, AccessType accessType) {
        this.searchKeyword = searchKeyword;
        this.accessType = accessType;
    }

    @Builder
    public WorkbookCriteria(String keyword, String access) {
        this(SearchKeyword.from(keyword), AccessType.from(access));
    }

    public boolean isNoSearchKeyword() {
        return searchKeyword.isNoKeyword();
    }

    public boolean isPublicAccess() {
        return accessType.isPublic();
    }

    public boolean isPrivateAccess() {
        return accessType.isPrivate();
    }

    public boolean isAllAccess() {
        return accessType.isAll();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkbookCriteria that = (WorkbookCriteria) o;
        return Objects.equals(getSearchKeyword(), that.getSearchKeyword()) && getAccessType() == that.getAccessType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSearchKeyword(), getAccessType());
    }
}
