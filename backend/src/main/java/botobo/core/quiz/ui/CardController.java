package botobo.core.quiz.ui;

import botobo.core.quiz.application.CardService;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PutMapping("/next-quiz")
    public ResponseEntity<Void> selectNextQuizCards(@RequestBody NextQuizCardsRequest nextQuizCardsRequest) {
        cardService.selectNextQuizCards(nextQuizCardsRequest);
        return ResponseEntity.noContent().build();
    }
}
