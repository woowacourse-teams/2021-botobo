package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.card.CardUpdateRequest;
import botobo.core.dto.card.CardUpdateResponse;
import botobo.core.dto.card.NextQuizCardsRequest;
import botobo.core.exception.NotAuthorException;
import botobo.core.exception.card.CardNotFoundException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final WorkbookRepository workbookRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, WorkbookRepository workbookRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.workbookRepository = workbookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CardResponse createCard(CardRequest cardRequest, AppUser appUser) {
        User author = findUser(appUser);
        Card card = convertToCard(cardRequest, author);
        cardRepository.save(card);
        return CardResponse.of(card);
    }

    private User findUser(AppUser appUser) {
        return userRepository.findById(appUser.getId()).orElseThrow(UserNotFoundException::new);
    }

    private Card convertToCard(CardRequest cardRequest, User author) {
        Workbook workbook = findWorkbook(cardRequest.getWorkbookId());
        if (!workbook.isAuthorOf(author)) {
            throw new NotAuthorException();
        }
        return cardRequest.toCardWithWorkbook(workbook);
    }

    private Workbook findWorkbook(Long workbookId) {
        return workbookRepository.findById(workbookId)
                .orElseThrow(WorkbookNotFoundException::new);
    }

    @Transactional
    public CardUpdateResponse updateCard(Long id,
                                         CardUpdateRequest cardUpdateRequest,
                                         AppUser appUser) {
        final User author = findUser(appUser);
        Card card = findCardOfAuthor(id, author);
        card.update(cardUpdateRequest.toCard());
        return CardUpdateResponse.of(card);
    }

    private Card findCardOfAuthor(Long id, User author) {
        final Card findCard = cardRepository.findById(id).orElseThrow(CardNotFoundException::new);
        if (!findCard.isAuthorOf(author)) {
            throw new NotAuthorException();
        }
        return findCard;
    }

    @Transactional
    public void deleteCard(Long id, AppUser appUser) {
        final User author = findUser(appUser);
        final Card card = findCardOfAuthor(id, author);
        card.delete();
    }

    // TODO nextQuizCard도 Interceptor 타도록 변경
    @Transactional
    public void selectNextQuizCards(NextQuizCardsRequest nextQuizCardsRequest) {
        List<Card> cards = cardRepository.findByIdIn(nextQuizCardsRequest.getCardIds());
        cards.forEach(Card::makeNextQuiz);
    }
}
