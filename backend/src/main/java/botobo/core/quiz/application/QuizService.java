package botobo.core.quiz.application;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
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

    public QuizService(WorkbookRepository workbookRepository) {
        this.workbookRepository = workbookRepository;
    }

    public List<QuizResponse> createQuiz(List<Long> ids) {
        List<Card> quizzes = new ArrayList<>();
        for (Long id : ids) {
            final Workbook workbook = workbookRepository.findById(id)
                    .orElseThrow(WorkbookNotFoundException::new);
            quizzes.addAll(workbook.getAllCards());
        }
        final int maxLimit = Math.min(quizzes.size(), 10);
        Collections.shuffle(quizzes);
        return quizzes.stream().limit(maxLimit)
                .map(QuizResponse::of)
                .collect(Collectors.toList());
    }
}
