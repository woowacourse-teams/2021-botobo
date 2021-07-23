package botobo.core.quiz.application;

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

    private final WorkbookRepository workbookRepository;
    private final CardRepository cardRepository;

    public QuizService(WorkbookRepository workbookRepository, CardRepository cardRepository) {
        this.workbookRepository = workbookRepository;
        this.cardRepository = cardRepository;
    }

    public List<QuizResponse> createQuiz(List<Long> workbookIds) {
        validateWorkbookIds(workbookIds);
        int counts = 10; // 퀴즈 개수 기능이 추가되면 인자로 받을 예정!
        final Quiz preparedQuizSet = new Quiz(cardRepository.findCardsByWorkbookId(workbookIds), counts);
        final Cards quiz = preparedQuizSet.makeQuiz();
        validateQuizIsEmpty(quiz);
        return QuizResponse.cardsOf(quiz);
    }

    private void validateQuizIsEmpty(Cards quiz) {
        if (quiz.isEmpty()) {
            throw new QuizEmptyException();
        }
    }

    private void validateWorkbookIds(List<Long> workbookIds) {
        // Opinion: id를 예외에 넘겨서 로그에 남겨도 좋을 것 같음.
        for (Long id : workbookIds) {
            if (!workbookRepository.existsById(id)) {
                throw new WorkbookNotFoundException();
            }
        }
    }

    public List<QuizResponse> createQuizForGuest() {
        Cards guestCards = GuestCards.getInstance();
        return QuizResponse.cardsOf(guestCards);
    }
}
