package botobo.core.category.application;

import botobo.core.card.domain.Card;
import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import botobo.core.category.dto.QuizResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuizService {

    private final CategoryRepository categoryRepository;

    public QuizService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<QuizResponse> createQuiz(List<Long> ids) {
        List<Card> quizzes = new ArrayList<>();
        for (Long id : ids) {
            final Category category = categoryRepository.findById(id)
                    .orElseThrow(IllegalArgumentException::new);
            quizzes.addAll(category.getCards());
        }
        Collections.shuffle(quizzes);
        List<QuizResponse> quizResponses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Card card = quizzes.get(i);
            quizResponses.add(QuizResponse.of(card));
        }
        return quizResponses;
    }
}
