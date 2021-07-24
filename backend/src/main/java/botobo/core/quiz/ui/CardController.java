package botobo.core.quiz.ui;

import botobo.core.quiz.application.CardService;
import botobo.core.quiz.dto.CardRequest;
import botobo.core.quiz.dto.CardResponse;
import botobo.core.quiz.dto.CardUpdateRequest;
import botobo.core.quiz.dto.CardUpdateResponse;
import botobo.core.quiz.dto.NextQuizCardsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PutMapping("/{id}")
    public ResponseEntity<CardUpdateResponse> updateCard(@PathVariable Long id, @Valid @RequestBody CardUpdateRequest cardUpdateRequest) {
        CardUpdateResponse cardUpdateResponse = cardService.updateCard(id, cardUpdateRequest);
        return ResponseEntity.ok(cardUpdateResponse);
    }

    @PutMapping("/next-quiz")
    public ResponseEntity<Void> selectNextQuizCards(@Valid @RequestBody NextQuizCardsRequest nextQuizCardsRequest) {
        cardService.selectNextQuizCards(nextQuizCardsRequest);
        return ResponseEntity.noContent().build();
    }
}
