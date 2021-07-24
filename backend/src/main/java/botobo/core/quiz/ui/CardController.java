package botobo.core.quiz.ui;

import botobo.core.quiz.application.CardService;
import botobo.core.quiz.dto.CardRequest;
import botobo.core.quiz.dto.CardResponse;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardRequest cardRequest) {
        CardResponse cardResponse = cardService.createCard(cardRequest);
        return ResponseEntity.created(URI.create("/api/cards/" + cardResponse.getId())).body(cardResponse);
    }

    @PutMapping("/next-quiz")
    public ResponseEntity<Void> selectNextQuizCards(@RequestBody NextQuizCardsRequest nextQuizCardsRequest) {
        cardService.selectNextQuizCards(nextQuizCardsRequest);
        return ResponseEntity.noContent().build();
    }
}
