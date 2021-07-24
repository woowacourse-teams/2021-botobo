package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.CardRequest;
import botobo.core.quiz.dto.CardResponse;
import botobo.core.quiz.dto.CardUpdateRequest;
import botobo.core.quiz.dto.CardUpdateResponse;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import botobo.core.quiz.exception.CardNotFoundException;
import botobo.core.quiz.exception.WorkbookNotFoundException;
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
        Workbook workbook = workbookRepository.findById(cardRequest.getWorkbookId())
                .orElseThrow(WorkbookNotFoundException::new);
        Card card = cardRequest.toCardWithWorkbook(workbook);
        cardRepository.save(card);
        return CardResponse.of(card);
    }

    @Transactional
    public CardUpdateResponse updateCard(Long id, CardUpdateRequest cardUpdateRequest) {
        Card card = cardRepository.findById(id)
                .orElseThrow(CardNotFoundException::new);
        card.updateFrom(cardUpdateRequest.toCard());
        return CardUpdateResponse.of(card);
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
