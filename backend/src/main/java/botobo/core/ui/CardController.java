package botobo.core.ui;

import botobo.core.application.CardService;
import botobo.core.domain.user.AppUser;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.card.CardUpdateRequest;
import botobo.core.dto.card.CardUpdateResponse;
import botobo.core.dto.card.NextQuizCardsRequest;
import botobo.core.ui.auth.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardRequest cardRequest, @AuthenticationPrincipal AppUser appUser) {
        CardResponse cardResponse = cardService.createCard(cardRequest, appUser);
        return ResponseEntity.created(URI.create("/api/cards/" + cardResponse.getId())).body(cardResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardUpdateResponse> updateCard(@PathVariable Long id, @Valid @RequestBody CardUpdateRequest cardUpdateRequest) {
        CardUpdateResponse cardUpdateResponse = cardService.updateCard(id, cardUpdateRequest);
        return ResponseEntity.ok(cardUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/next-quiz")
    public ResponseEntity<Void> selectNextQuizCards(@Valid @RequestBody NextQuizCardsRequest nextQuizCardsRequest) {
        cardService.selectNextQuizCards(nextQuizCardsRequest);
        return ResponseEntity.noContent().build();
    }
}
