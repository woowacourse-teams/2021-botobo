package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookFinder;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.domain.workbook.criteria.SearchKeyword;
import botobo.core.domain.workbook.criteria.WorkbookCriteria;
import botobo.core.dto.card.ScrapCardRequest;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
import botobo.core.exception.NotAuthorException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.card.CardNotFoundException;
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
    private final CardRepository cardRepository;

    public WorkbookService(WorkbookRepository workbookRepository, UserRepository userRepository, CardRepository cardRepository) {
        this.workbookRepository = workbookRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
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
        Workbook workbook = findWorkbook(id);
        workbook.updateIfUserIsAuthor(workbookUpdateRequest.getName(),
                workbookUpdateRequest.isOpened(),
                appUser.getId());
        return WorkbookResponse.authorOf(workbook);
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

    @Transactional
    public void deleteWorkbook(Long id, AppUser appUser) {
        Workbook workbook = findWorkbook(id);
        workbook.deleteIfUserIsAuthor(appUser.getId());
    }

    @Transactional
    public void scrapSelectedCardsToWorkbook(Long workbookId, ScrapCardRequest scrapCardRequest, AppUser appUser) {
        final User user = findUser(appUser);
        final Workbook workbook = findWorkbook(workbookId);
        if (!workbook.isAuthorOf(user)) {
            throw new NotAuthorException();
        }
        saveScrappedCardsToWorkbook(scrapCardRequest.getCardIds(), workbook);
    }

    private void saveScrappedCardsToWorkbook(List<Long> cardIds, Workbook targetWorkbook) {
        for (Long id: cardIds) {
            final Card sourceCard = cardRepository.findById(id).orElseThrow(CardNotFoundException::new);
            Card.createCopyAndLinkToWorkbook(sourceCard, targetWorkbook);
        }
    }

    private Workbook findWorkbook(Long workbookId) {
        return workbookRepository.findById(workbookId)
                .orElseThrow(WorkbookNotFoundException::new);
    }

    private User findUser(AppUser appUser) {
        return userRepository.findById(appUser.getId())
                .orElseThrow(UserNotFoundException::new);
    }
}
