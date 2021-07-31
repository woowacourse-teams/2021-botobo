package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookFinder;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.domain.workbook.criteria.SearchKeyword;
import botobo.core.domain.workbook.criteria.WorkbookCriteria;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
import botobo.core.exception.NotAuthorException;
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
        User user = findUser(appUser);
        Workbook workbook = workbookRequest.toWorkbook()
                .createBy(user);
        Workbook savedWorkbook = workbookRepository.save(workbook);
        return WorkbookResponse.authorOf(savedWorkbook);
    }

    @Transactional
    public WorkbookResponse updateWorkbook(Long id, WorkbookUpdateRequest workbookUpdateRequest, AppUser appUser) {
        User user = findUser(appUser);
        Workbook workbook = findWorkbook(id);

        validateAuthor(user, workbook);

        workbook.update(workbookUpdateRequest.toWorkbook());
        return WorkbookResponse.authorOf(workbook);
    }

    @Transactional
    public void deleteWorkbook(Long id, AppUser appUser) {
        User user = findUser(appUser);
        Workbook workbook = findWorkbook(id);

        validateAuthor(user, workbook);

        workbook.delete();
    }

    public List<WorkbookResponse> findWorkbooksByUser(AppUser appUser) {
        if (appUser.isAnonymous()) {
            return WorkbookResponse.authorListOf(Collections.emptyList());
        }
        return WorkbookResponse.authorListOf(
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

    private User findUser(AppUser appUser) {
        return userRepository.findById(appUser.getId())
                .orElseThrow(UserNotFoundException::new);
    }

    private Workbook findWorkbook(Long id) {
        return workbookRepository.findById(id)
                .orElseThrow(WorkbookNotFoundException::new);
    }

    private void validateAuthor(User user, Workbook workbook) {
        if (!workbook.isCreatedBy(user)) {
            throw new NotAuthorException();
        }
    }
}
