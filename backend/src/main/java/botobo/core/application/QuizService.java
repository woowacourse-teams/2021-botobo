package botobo.core.application;

import botobo.core.domain.card.Card;
import botobo.core.domain.card.CardRepository;
import botobo.core.domain.card.Cards;
import botobo.core.domain.card.GuestCards;
import botobo.core.domain.card.Quiz;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.card.QuizRequest;
import botobo.core.dto.card.QuizResponse;
import botobo.core.exception.user.NotAuthorException;
import botobo.core.exception.workbook.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuizService extends AbstractUserService {
    private static final int DEFAULT_QUIZ_COUNT = 10;

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
        User user = findUser(appUser);
        final List<Long> workbookIds = quizRequest.getWorkbookIds();
        final int count = quizRequest.getCount();

        validateWorkbooks(workbookIds, user);

        final Cards quiz = makeQuiz(cardRepository.findCardsByWorkbookIds(workbookIds), count)
                .postProcess();
        return QuizResponse.cardsOf(quiz);
    }

    private void validateWorkbooks(List<Long> workbookIds, User user) {
        for (Long id : workbookIds) {
            validateAuthor(findWorkbookById(id), user);
        }
    }

    private Workbook findWorkbookById(Long workbookId) {
        return workbookRepository.findById(workbookId).orElseThrow(WorkbookNotFoundException::new);
    }

    private void validateAuthor(Workbook workbook, User user) {
        if (!workbook.isAuthorOf(user)) {
            throw new NotAuthorException();
        }
    }

    public List<QuizResponse> createQuizFromWorkbook(Long workbookId, AppUser appUser) {
        findUser(appUser);
        validateWorkbook(workbookId);
        final Cards quiz = makeQuiz(cardRepository.findCardsByWorkbookId(workbookId), DEFAULT_QUIZ_COUNT);
        return QuizResponse.cardsOf(quiz);
    }

    private Cards makeQuiz(List<Card> cards, int counts) {
        final Quiz preparedQuizSet = new Quiz(cards, counts);
        return preparedQuizSet.makeQuiz();
    }

    private void validateWorkbook(Long workbookId) {
        if (!workbookRepository.existsByIdAndOpenedTrue(workbookId)) {
            throw new WorkbookNotFoundException();
        }
    }
}
