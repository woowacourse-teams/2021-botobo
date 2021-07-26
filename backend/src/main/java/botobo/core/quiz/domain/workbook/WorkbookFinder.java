package botobo.core.quiz.domain.workbook;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookFinder {

    private List<Workbook> workbooks;

    public List<Workbook> apply(WorkbookCriteria workbookCriteria) {
        return workbooks.stream()
                .filter(filterAccessType(workbookCriteria))
                .filter(filterSearchKeyword(workbookCriteria))
                .collect(Collectors.toList());
    }

    private Predicate<Workbook> filterAccessType(WorkbookCriteria workbookCriteria) {
        return workbook -> (workbookCriteria.isAllAccess() ||
                (workbook.isPublic() && workbookCriteria.isPublicAccess()) ||
                (workbook.isPrivate() && workbookCriteria.isPrivateAccess())
        );
    }

    private Predicate<Workbook> filterSearchKeyword(WorkbookCriteria workbookCriteria) {
        return workbook -> (workbookCriteria.isNoSearchKeyword() ||
                (workbook.containsWord(workbookCriteria.getSearchKeywordValue()))
        );
    }
}
