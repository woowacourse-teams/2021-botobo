package botobo.core.quiz.domain.workbook;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class WorkbookCriteria {

    private SearchKeyword searchKeyword = SearchKeyword.from(null);
    private AccessType accessType = AccessType.PUBLIC;

    @Builder
    private WorkbookCriteria(SearchKeyword searchKeyword, AccessType accessType) {
        if (Objects.nonNull(searchKeyword)) {
            this.searchKeyword = searchKeyword;
        }
        if (Objects.nonNull(accessType)) {
            this.accessType = accessType;
        }
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

    public String getSearchKeywordValue() {
        return searchKeyword.getValue();
    }
}
