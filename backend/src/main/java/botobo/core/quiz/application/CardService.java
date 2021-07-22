package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public void selectNextQuizCards(NextQuizCardsRequest nextQuizCardsRequest) {
        List<Card> nextQuizCards = cardRepository.findByIdIn(nextQuizCardsRequest.getCardIds());
        nextQuizCards.forEach(Card::makeNextQuizCard);
    }
}
