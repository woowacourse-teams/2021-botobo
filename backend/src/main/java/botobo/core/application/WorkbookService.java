package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.SearchKeyword;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookCriteria;
import botobo.core.domain.workbook.WorkbookFinder;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkbookService {

    private final WorkbookRepository workbookRepository;
    private final UserRepository userRepository;

    public WorkbookService(WorkbookRepository workbookRepository, UserRepository userRepository) {
        this.workbookRepository = workbookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WorkbookResponse createWorkbookByUser(WorkbookRequest workbookRequest, AppUser appUser) {
        User user = userRepository.findById(appUser.getId())
                .orElseThrow(UserNotFoundException::new);
        Workbook workbook = Workbook.builder()
                .name(workbookRequest.getName())
                .opened(workbookRequest.isOpened())
                .deleted(false)
                .user(user)
                .build();
        Workbook savedWorkbook = workbookRepository.save(workbook);
        return WorkbookResponse.convert(savedWorkbook);
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
