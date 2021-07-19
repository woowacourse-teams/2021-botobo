package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
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

    public QuizService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<QuizResponse> createQuiz(List<Long> ids) {
        List<Card> quizzes = new ArrayList<>();
        for (Long id : ids) {
            final Category category = categoryRepository.findById(id)
                    .orElseThrow(CategoryNotFoundException::new);
            quizzes.addAll(category.getAllCards());
        }
        final int maxLimit = Math.min(quizzes.size(), 10);
        Collections.shuffle(quizzes);
        return quizzes.stream().limit(maxLimit)
                .map(QuizResponse::of)
                .collect(Collectors.toList());
    }
}
