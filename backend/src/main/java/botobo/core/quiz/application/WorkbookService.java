package botobo.core.quiz.application;

import botobo.core.quiz.domain.workbook.SearchKeyword;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookCriteria;
import botobo.core.quiz.domain.workbook.WorkbookFinder;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.workbook.WorkbookCardResponse;
import botobo.core.quiz.dto.workbook.WorkbookResponse;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import botobo.core.user.domain.AppUser;
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
            return WorkbookResponse.listOf(Collections.emptyList());
        }
        return WorkbookResponse.listOf(
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

        return WorkbookResponse.listOf(workbooks);
    }

    public WorkbookCardResponse findWorkbookCardsById(Long id) {
        Workbook workbook = workbookRepository.findById(id)
                .orElseThrow(WorkbookNotFoundException::new);
        return WorkbookCardResponse.of(workbook);
    }
}
