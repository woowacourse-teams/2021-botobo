package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.card.Cards;
import botobo.core.domain.card.GuestCards;
import botobo.core.domain.card.Quiz;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.QuizRequest;
import botobo.core.dto.card.QuizResponse;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuizService extends AbstractUserService {
    private static final int GUEST_QUIZ_COUNT = 10;

    private final WorkbookRepository workbookRepository;
    private final CardRepository cardRepository;

    public QuizService(WorkbookRepository workbookRepository, CardRepository cardRepository, UserRepository userRepository) {
        super(userRepository);
        this.workbookRepository = workbookRepository;
        this.cardRepository = cardRepository;
    }

    public List<QuizResponse> createQuizForGuest() {
        Cards guestCards = GuestCards.getInstance();
        return QuizResponse.cardsOf(guestCards);
    }

    @Transactional
    public List<QuizResponse> createQuiz(QuizRequest quizRequest, AppUser appUser) {
        findUser(appUser);
        final List<Long> workbookIds = quizRequest.getWorkbookIds();
        final int count = quizRequest.getCount();
        validateWorkbookIds(workbookIds);
        final Cards quiz = makeQuiz(cardRepository.findCardsByWorkbookIds(workbookIds), count)
                .postProcess();
        return QuizResponse.cardsOf(quiz);
    }

    public List<QuizResponse> createQuizFromWorkbook(Long workbookId) {
        validateWorkbook(workbookId);
        final Cards quiz = makeQuiz(cardRepository.findCardsByWorkbookId(workbookId), GUEST_QUIZ_COUNT);
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
