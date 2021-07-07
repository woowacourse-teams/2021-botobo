package botobo.core.category.application;

import botobo.core.card.domain.Card;
import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import botobo.core.category.dto.QuizResponse;
import botobo.core.category.exception.CategoryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                    .orElseThrow(CategoryNotFoundException::new);
            quizzes.addAll(category.getCards());
        }
        Collections.shuffle(quizzes);
        return IntStream.range(0, 10)
                .mapToObj(quizzes::get)
                .map(QuizResponse::of)
                .collect(Collectors.toList());
    }
}
