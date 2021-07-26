package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.card.Cards;
import botobo.core.domain.card.GuestCards;
import botobo.core.domain.card.Quiz;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.QuizResponse;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuizService {
    private static final int DEFAULT_QUIZ_COUNT = 10;

    private final WorkbookRepository workbookRepository;
    private final CardRepository cardRepository;

    public QuizService(WorkbookRepository workbookRepository, CardRepository cardRepository) {
        this.workbookRepository = workbookRepository;
        this.cardRepository = cardRepository;
    }

    public List<QuizResponse> createQuizForGuest() {
        Cards guestCards = GuestCards.getInstance();
        return QuizResponse.cardsOf(guestCards);
    }

    public List<QuizResponse> createQuiz(List<Long> workbookIds) {
        validateWorkbookIds(workbookIds);
        final Cards quiz = makeQuiz(cardRepository.findCardsByWorkbookIds(workbookIds), DEFAULT_QUIZ_COUNT)
                .postProcess();
        return QuizResponse.cardsOf(quiz);
    }

    public List<QuizResponse> createQuizFromWorkbook(Long workbookId) {
        validateWorkbook(workbookId);
        final Cards quiz = makeQuiz(cardRepository.findCardsByWorkbookId(workbookId), DEFAULT_QUIZ_COUNT);
        return QuizResponse.cardsOf(quiz);
    }

    private Cards makeQuiz(List<Card> cards, int counts) {
        final Quiz preparedQuizSet = new Quiz(cards, counts);
        return preparedQuizSet.makeQuiz();
    }

    private void validateWorkbookIds(List<Long> workbookIds) {
        // Opinion: id를 예외에 넘겨서 로그에 남겨도 좋을 것 같음.
        for (Long id : workbookIds) {
            validateWorkbookId(id);
        }
    }

    private void validateWorkbookId(Long workbookId) {
        if (!workbookRepository.existsById(workbookId)) {
            throw new WorkbookNotFoundException();
        }
    }

    private void validateWorkbook(Long workbookId) {
        if (!workbookRepository.existsByIdAndOpenedTrue(workbookId)) {
            throw new WorkbookNotFoundException();
        }
    }
}
