package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.card.FixedCards;
import botobo.core.quiz.domain.category.Category;
import botobo.core.quiz.domain.category.CategoryRepository;
import botobo.core.quiz.dto.QuizResponse;
import botobo.core.quiz.exception.CategoryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuizService {

    private final CategoryRepository categoryRepository;
    private final CardRepository cardRepository;

    public QuizService(CategoryRepository categoryRepository, CardRepository cardRepository) {
        this.categoryRepository = categoryRepository;
        this.cardRepository = cardRepository;
    }

    public List<QuizResponse> createQuiz(List<Long> ids) {
        List<Card> quiz = new ArrayList<>();
        for (Long id : ids) {
            final Category category = categoryRepository.findById(id)
                    .orElseThrow(CategoryNotFoundException::new);
            quiz.addAll(category.getAllCards());
        }
        final int maxLimit = Math.min(quiz.size(), 10);
        Collections.shuffle(quiz);
        return convertToQuizResponse(quiz, maxLimit);
    }

    public List<QuizResponse> createQuizForGuest() {
        FixedCards fixedCards = FixedCards.getInstance();
        final int maxLimit = 10;
        if (fixedCards.isFull()) {
            return convertToQuizResponse(fixedCards.getCards(), maxLimit);
        }
        List<Card> quiz = cardRepository.findFirst10By();
        fixedCards.initialize(quiz);
        return convertToQuizResponse(quiz, maxLimit);
    }

    public List<QuizResponse> convertToQuizResponse(List<Card> cards, int maxLimit) {
        return cards.stream().limit(maxLimit)
                .map(QuizResponse::of)
                .collect(Collectors.toList());
    }
}
