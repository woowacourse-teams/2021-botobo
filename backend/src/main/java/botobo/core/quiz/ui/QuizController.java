package botobo.core.quiz.ui;

import botobo.core.quiz.application.QuizService;
import botobo.core.quiz.dto.CategoryIdsRequest;
import botobo.core.quiz.dto.QuizResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/quizzes")
    public ResponseEntity<List<QuizResponse>> createQuiz(@Valid @RequestBody CategoryIdsRequest categoryIdsRequest) {
        List<QuizResponse> quizResponses = quizService.createQuiz(categoryIdsRequest.getCategoryIds());
        return ResponseEntity.ok(quizResponses);
    }
}
