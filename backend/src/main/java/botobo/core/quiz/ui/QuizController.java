package botobo.core.quiz.ui;

import botobo.core.quiz.application.QuizService;
import botobo.core.quiz.dto.QuizRequest;
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
    public ResponseEntity<List<QuizResponse>> createQuiz(@Valid @RequestBody QuizRequest quizRequest) {
        List<QuizResponse> quizResponses = quizService.createQuiz(quizRequest.getWorkbookIds());
        return ResponseEntity.ok(quizResponses);
    }
}
