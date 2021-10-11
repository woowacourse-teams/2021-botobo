package botobo.core.ui;

import botobo.core.application.WorkbookService;
import botobo.core.domain.user.AppUser;
import botobo.core.dto.card.ScrapCardRequest;
import botobo.core.dto.heart.HeartResponse;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.dto.workbook.WorkbookUpdateRequest;
import botobo.core.ui.auth.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/workbooks")
public class WorkbookController {

    private final WorkbookService workbookService;

    public WorkbookController(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }

    @PostMapping
    public ResponseEntity<WorkbookResponse> createWorkbook(@RequestBody @Valid WorkbookRequest workbookRequest,
                                                           @AuthenticationPrincipal AppUser appUser) {
        WorkbookResponse workbookResponse = workbookService.createWorkbookByUser(workbookRequest, appUser);
        return ResponseEntity.created(URI.create("/workbooks/" + workbookResponse.getId() + "/cards")).body(workbookResponse);
    }

    @GetMapping
    public ResponseEntity<List<WorkbookResponse>> findWorkbooksByUser(@AuthenticationPrincipal AppUser appUser) {
        return ResponseEntity.ok(
                workbookService.findWorkbooksByUser(appUser)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkbookResponse> updateWorkbook(@PathVariable Long id,
                                                           @RequestBody @Valid WorkbookUpdateRequest workbookUpdateRequest,
                                                           @AuthenticationPrincipal AppUser appUser) {
        return ResponseEntity.ok(
                workbookService.updateWorkbook(id, workbookUpdateRequest, appUser)
        );
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<WorkbookCardResponse> findPublicWorkbookById(@PathVariable Long id,
                                                                       @AuthenticationPrincipal AppUser appUser) {
        return ResponseEntity.ok(
                workbookService.findPublicWorkbookById(id, appUser)
        );
    }

    @GetMapping("/{workbookId}/cards")
    public ResponseEntity<WorkbookCardResponse> findWorkbookCardsById(@PathVariable Long workbookId,
                                                                      @AuthenticationPrincipal AppUser appUser) {
        return ResponseEntity.ok(
                workbookService.findWorkbookCardsById(workbookId, appUser)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkbook(@PathVariable Long id,
                                               @AuthenticationPrincipal AppUser appUser) {
        workbookService.deleteWorkbook(id, appUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{workbookId}/cards")
    public ResponseEntity<Void> scrapSelectedCardsToWorkbook(@PathVariable Long workbookId,
                                                             @Valid @RequestBody ScrapCardRequest scrapCardRequest,
                                                             @AuthenticationPrincipal AppUser appUser) {
        workbookService.scrapSelectedCardsToWorkbook(workbookId, scrapCardRequest, appUser);
        return ResponseEntity.created(URI.create(String.format("/workbooks/%d/cards", workbookId))).build();
    }

    @PutMapping("/{workbookId}/hearts")
    public ResponseEntity<HeartResponse> toggleHeart(@PathVariable Long workbookId, @AuthenticationPrincipal AppUser appUser) {
        return ResponseEntity.ok(
                workbookService.toggleHeart(workbookId, appUser)
        );
    }

    @GetMapping("/public")
    public ResponseEntity<List<WorkbookResponse>> findPublicWorkbooks() {
        return ResponseEntity.ok(
                workbookService.findPublicWorkbooks()
        );
    }
}
