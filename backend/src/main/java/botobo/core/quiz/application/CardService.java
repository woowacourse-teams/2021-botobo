package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.CardRequest;
import botobo.core.quiz.dto.CardResponse;
import botobo.core.quiz.dto.NextQuizCardsRequest;
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
    public void selectNextQuizCards(NextQuizCardsRequest nextQuizCardsRequest) {
        List<Card> cards = cardRepository.findByIdIn(nextQuizCardsRequest.getCardIds());
        cards.forEach(Card::makeNextQuizCard);
    }
}
