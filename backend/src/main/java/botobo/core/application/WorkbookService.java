package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.card.Cards;
import botobo.core.domain.heart.Heart;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.ScrapCardRequest;
import botobo.core.dto.heart.HeartResponse;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
import botobo.core.exception.card.CardNotFoundException;
import botobo.core.exception.user.NotAuthorException;
import botobo.core.exception.workbook.NotOpenedWorkbookException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class WorkbookService extends AbstractUserService {

    private final WorkbookRepository workbookRepository;
    private final CardRepository cardRepository;
    private final TagService tagService;

    public WorkbookService(WorkbookRepository workbookRepository, UserRepository userRepository,
                           CardRepository cardRepository, TagService tagService) {
        super(userRepository);
        this.workbookRepository = workbookRepository;
        this.cardRepository = cardRepository;
        this.tagService = tagService;
    }

    @Transactional
    public WorkbookResponse createWorkbookByUser(WorkbookRequest workbookRequest, AppUser appUser) {
        User user = findUser(appUser);
        Tags tags = tagService.convertTags(workbookRequest.getTags());
        Workbook workbook = workbookRequest.toWorkbook()
                .createBy(user)
                .taggedBy(tags);
        Workbook savedWorkbook = workbookRepository.save(workbook);
        return WorkbookResponse.authorOf(savedWorkbook);
    }

    @Transactional
    public WorkbookResponse updateWorkbook(Long id, WorkbookUpdateRequest workbookUpdateRequest, AppUser appUser) {
        User user = findUser(appUser);
        Workbook workbook = findWorkbook(id);

        validateAuthor(user, workbook);

        Tags tags = tagService.convertTags(workbookUpdateRequest.getTags());
        workbook.update(workbookUpdateRequest.toWorkbookWithTags(tags));
        workbookRepository.flush();
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

    public WorkbookCardResponse findWorkbookCardsById(Long id, AppUser appUser) {
        User user = findUser(appUser);
        Workbook workbook = findWorkbookByIdAndOrderCardByNew(id);
        validateAuthor(user, workbook);
        return WorkbookCardResponse.ofUserWorkbook(workbook);
    }

    private Workbook findWorkbookByIdAndOrderCardByNew(Long id) {
        return workbookRepository.findByIdAndOrderCardByNew(id)
                .orElseThrow(WorkbookNotFoundException::new);
    }

    public WorkbookCardResponse findPublicWorkbookById(Long id, AppUser appUser) {
        Workbook workbook = findWorkbookByIdAndOrderCardByNew(id);
        if (!workbook.isOpened()) {
            throw new NotOpenedWorkbookException();
        }
        boolean heartExists = workbook.existsHeartByAppUser(appUser);
        return WorkbookCardResponse.ofOpenedWorkbook(workbook, heartExists);
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

    @Transactional
    public HeartResponse toggleHeart(Long workbookId, AppUser appUser) {
        Long userId = appUser.getId();
        Workbook workbook = findWorkbook(workbookId);
        Heart heart = Heart.builder()
                .workbook(workbook)
                .userId(userId)
                .build();
        return HeartResponse.of(
                workbook.toggleHeart(heart)
        );
    }

    public List<WorkbookResponse> findPublicWorkbooks() {
        List<Workbook> workbooks = workbookRepository.findRandomPublicWorkbooks();
        return WorkbookResponse.simpleListOf(workbooks);
    }
}
