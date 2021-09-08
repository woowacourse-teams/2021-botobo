package botobo.core.ui;

import botobo.core.application.QuizService;
import botobo.core.domain.user.AppUser;
import botobo.core.dto.card.QuizRequest;
import botobo.core.dto.card.QuizResponse;
import botobo.core.ui.auth.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<List<QuizResponse>> createQuiz(@Valid @RequestBody QuizRequest quizRequest,
                                                         @AuthenticationPrincipal AppUser appUser) {
        List<QuizResponse> quizResponses = quizService.createQuiz(quizRequest, appUser);
        return ResponseEntity.ok(quizResponses);
    }

    @GetMapping("/guest")
    public ResponseEntity<List<QuizResponse>> createQuizForGuest() {
        List<QuizResponse> quizResponses = quizService.createQuizForGuest();
        return ResponseEntity.ok(quizResponses);
    }

    @GetMapping("/{workbookId}")
    public ResponseEntity<List<QuizResponse>> createQuizFromWorkbook(@PathVariable Long workbookId,
                                                                     @AuthenticationPrincipal AppUser appUser) {
        List<QuizResponse> quizResponses = quizService.createQuizFromWorkbook(workbookId, appUser);
        return ResponseEntity.ok(quizResponses);
    }
}
