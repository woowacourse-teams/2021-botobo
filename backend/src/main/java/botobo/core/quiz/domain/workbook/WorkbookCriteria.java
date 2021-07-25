package botobo.core.quiz.domain.workbook;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookCriteria {

    private SearchKeyword searchKeyword;
    private AccessType accessType;
    private OwnerType ownerType;

    @Builder
    public WorkbookCriteria(String keyword, String access, String owner) {
        this(SearchKeyword.from(keyword), AccessType.from(access), OwnerType.from(owner));
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

    public boolean isMineType() {
        return ownerType.isMine();
    }

    public String getSearchKeywordValue() {
        return searchKeyword.getValue();
    }
}
