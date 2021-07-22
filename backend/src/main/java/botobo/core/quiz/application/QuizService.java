package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.card.Cards;
import botobo.core.quiz.domain.card.GuestCards;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.QuizResponse;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuizService {

    private final WorkbookRepository workbookRepository;
    private final CardRepository cardRepository;

    public QuizService(WorkbookRepository workbookRepository, CardRepository cardRepository) {
        this.workbookRepository = workbookRepository;
        this.cardRepository = cardRepository;
    }

    public List<QuizResponse> createQuiz(List<Long> workbookIds) {
        validateWorkbookIds(workbookIds);

        final List<Card> nextShownCards = cardRepository.findNextCardAndWorkbookIdIn(workbookIds);
        Cards nextCards = new Cards(nextShownCards);
        if (nextCards.size() == 10) {
            nextShownCards.forEach(Card::cancelNextQuiz);
            return QuizResponse.cardsOf(nextCards);
        }

        Cards cards = new Cards(cardRepository.findCardsByWorkbookId(workbookIds)).shuffle();
        final int index = Math.min(cards.size(), 10 - nextCards.size());

        Cards quiz = new Cards(cards.subList(index));
        quiz.addAll(nextCards);
        nextShownCards.forEach(Card::cancelNextQuiz);
        return QuizResponse.cardsOf(quiz);
    }

    private void validateWorkbookIds(List<Long> workbookIds) {
        if (!workbookRepository.existsByIdIsIn(workbookIds)) {
            throw new WorkbookNotFoundException();
        }
    }

    public List<QuizResponse> createQuizForGuest() {
        Cards guestCards = GuestCards.getInstance();
        return QuizResponse.cardsOf(guestCards);
    }
}
