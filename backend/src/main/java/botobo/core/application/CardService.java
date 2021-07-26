package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.card.CardUpdateRequest;
import botobo.core.dto.card.CardUpdateResponse;
import botobo.core.dto.card.NextQuizCardsRequest;
import botobo.core.exception.card.CardNotFoundException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final WorkbookRepository workbookRepository;

    public CardService(CardRepository cardRepository, WorkbookRepository workbookRepository) {
        this.cardRepository = cardRepository;
        this.workbookRepository = workbookRepository;
    }

    @Transactional
    public CardResponse createCard(CardRequest cardRequest) {
        Card card = convertToCard(cardRequest);
        cardRepository.save(card);
        return CardResponse.of(card);
    }

    private Card convertToCard(CardRequest cardRequest) {
        Workbook workbook = findWorkbook(cardRequest.getWorkbookId());
        return cardRequest.toCardWithWorkbook(workbook);
    }

    private Workbook findWorkbook(Long workbookId) {
        return workbookRepository.findById(workbookId)
                .orElseThrow(WorkbookNotFoundException::new);
    }

    @Transactional
    public CardUpdateResponse updateCard(Long id, CardUpdateRequest cardUpdateRequest) {
        Card updateInfoCard = convertToCard(cardUpdateRequest);
        Card card = cardRepository.findById(id)
                .orElseThrow(CardNotFoundException::new);
        card.updateFrom(updateInfoCard);
        return CardUpdateResponse.of(card);
    }

    private Card convertToCard(CardUpdateRequest cardUpdateRequest) {
        Workbook workbook = findWorkbook(cardUpdateRequest.getWorkbookId());
        return cardUpdateRequest.toCardWithWorkbook(workbook);
    }

    @Transactional
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    @Transactional
    public void selectNextQuizCards(NextQuizCardsRequest nextQuizCardsRequest) {
        List<Card> cards = cardRepository.findByIdIn(nextQuizCardsRequest.getCardIds());
        cards.forEach(Card::makeNextQuiz);
    }
}
