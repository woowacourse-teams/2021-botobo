package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookFinder;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.domain.workbook.criteria.SearchKeyword;
import botobo.core.domain.workbook.criteria.WorkbookCriteria;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkbookService {

    private final WorkbookRepository workbookRepository;

    public WorkbookService(WorkbookRepository workbookRepository) {
        this.workbookRepository = workbookRepository;
    }

    public List<WorkbookResponse> findWorkbooksByUser(AppUser appUser) {
        if (appUser.isAnonymous()) {
            return WorkbookResponse.ownedListOf(Collections.emptyList());
        }
        return WorkbookResponse.ownedListOf(
                workbookRepository.findAllByUserId(appUser.getId())
        );
    }

    public List<WorkbookResponse> findPublicWorkbooksBySearch(String search) {
        WorkbookCriteria workbookCriteria = WorkbookCriteria.builder()
                .searchKeyword(SearchKeyword.from(search))
                .build();

        return findWorkbookByCriteria(workbookCriteria);
    }

    private List<WorkbookResponse> findWorkbookByCriteria(WorkbookCriteria workbookCriteria) {
        List<Workbook> workbooks = WorkbookFinder.builder()
                .workbooks(workbookRepository.findAll())
                .build()
                .apply(workbookCriteria);

        return WorkbookResponse.openedListOf(workbooks);
    }

    public WorkbookCardResponse findWorkbookCardsById(Long id) {
        Workbook workbook = workbookRepository.findByIdAndOrderCardByNew(id)
                .orElseThrow(WorkbookNotFoundException::new);
        return WorkbookCardResponse.of(workbook);
    }
}
