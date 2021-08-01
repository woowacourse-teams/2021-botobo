package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.card.Cards;
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
import botobo.core.exception.card.CardNotFoundException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    private void validateAuthor(User user, Workbook workbook) {
        if (!workbook.isAuthorOf(user)) {
            throw new NotAuthorException();
        }
    }

    @Transactional
    public void scrapSelectedCardsToWorkbook(Long workbookId, ScrapCardRequest scrapCardRequest, AppUser appUser) {
        User user = findUser(appUser);
        Workbook workbook = findWorkbook(workbookId);
        if (!workbook.isAuthorOf(user)) {
            throw new NotAuthorException();
        }
        Cards scrappedCards = new Cards(scrapCards(scrapCardRequest.distinctCardIds()));
        addScrappedCardsToWorkbook(workbook, scrappedCards);
    }

    private User findUser(AppUser appUser) {
        return userRepository.findById(appUser.getId())
                .orElseThrow(UserNotFoundException::new);
    }

    private Workbook findWorkbook(Long workbookId) {
        return workbookRepository.findById(workbookId)
                .orElseThrow(WorkbookNotFoundException::new);
    }

    private List<Card> scrapCards(List<Long> cardIds) {
        List<Card> cards = cardRepository.findByIdIn(cardIds);
        if (cardIds.size() != cards.size()) {
            throw new CardNotFoundException();
        }
        return cards.stream()
                .map(Card::createCopyOf)
                .collect(Collectors.toList());
    }

    private void addScrappedCardsToWorkbook(Workbook workbook, Cards scrappedCards) {
        workbook.addCards(scrappedCards);
    }
}
