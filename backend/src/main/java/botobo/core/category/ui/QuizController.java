package botobo.core.category.ui;

import botobo.core.category.application.QuizService;
import botobo.core.category.dto.CategoryIdsRequest;
import botobo.core.category.dto.QuizResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/quizzes")
    public ResponseEntity<List<QuizResponse>> createQuiz(@RequestBody CategoryIdsRequest categoryIdsRequest) {
        List<QuizResponse> quizResponses = quizService.createQuiz(categoryIdsRequest.getCategoryIds());
        return ResponseEntity.ok(quizResponses);
    }
}
