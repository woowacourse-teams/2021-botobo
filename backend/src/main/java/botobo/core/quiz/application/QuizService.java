package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.card.FixedCards;
import botobo.core.quiz.dto.QuizResponse;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuizService {

    private final WorkbookRepository workbookRepository;
    private final CardRepository cardRepository;

    public QuizService(WorkbookRepository workbookRepository, CardRepository cardRepository) {
        this.workbookRepository = workbookRepository;
        this.cardRepository = cardRepository;
    }

    public List<QuizResponse> createQuiz(List<Long> ids) {
        List<Card> cards = new ArrayList<>();
        for (Long id : ids) {
            final Workbook workbook = workbookRepository.findById(id)
                    .orElseThrow(WorkbookNotFoundException::new);
            cards.addAll(workbook.getAllCards());
        }
        final int maxLimit = Math.min(cards.size(), 10);
        Collections.shuffle(cards);
        final List<Card> quiz = cards.stream().limit(maxLimit).collect(Collectors.toList());
        return QuizResponse.listOf(quiz);
    }

    public List<QuizResponse> createQuizForGuest() {
        FixedCards fixedCards = FixedCards.getInstance();
        if (fixedCards.isFull()) {
            return QuizResponse.listOf(fixedCards.getCards());
        }
        final List<Card> quiz = cardRepository.findFirst10By();
        fixedCards.initialize(quiz);
        return QuizResponse.listOf(quiz);
    }
}
