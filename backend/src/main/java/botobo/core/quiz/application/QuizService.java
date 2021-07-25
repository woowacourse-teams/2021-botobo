package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.card.Cards;
import botobo.core.quiz.domain.card.GuestCards;
import botobo.core.quiz.domain.card.Quiz;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.QuizResponse;
import botobo.core.quiz.exception.QuizEmptyException;
import botobo.core.quiz.exception.WorkbookNotFoundException;
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
        final Cards quiz = makeQuiz(cardRepository.findCardsByWorkbookIds(workbookIds), DEFAULT_QUIZ_COUNT);
        return QuizResponse.cardsOf(quiz.incrementEncounterCount());
    }

    public List<QuizResponse> createQuizFromWorkbook(Long workbookId) {
        validateWorkbookId(workbookId);
        final Cards quiz = makeQuiz(cardRepository.findCardsByWorkbookId(workbookId), DEFAULT_QUIZ_COUNT);
        return QuizResponse.cardsOf(quiz);
    }

    private Cards makeQuiz(List<Card> cards, int counts) {
        final Quiz preparedQuizSet = new Quiz(cards, counts);
        final Cards quiz = preparedQuizSet.makeQuiz();
        validateQuizIsEmpty(quiz);
        return quiz;
    }

    private void validateQuizIsEmpty(Cards quiz) {
        if (quiz.isEmpty()) {
            throw new QuizEmptyException();
        }
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
}
