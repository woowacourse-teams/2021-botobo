package botobo.core.ui;

import botobo.core.application.WorkbookService;
import botobo.core.domain.user.AppUser;
import botobo.core.dto.workbook.WorkbookCardResponse;
import botobo.core.dto.workbook.WorkbookRequest;
import botobo.core.dto.workbook.WorkbookResponse;
import botobo.core.ui.auth.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/workbooks")
public class WorkbookController {

    private final WorkbookService workbookService;

    public WorkbookController(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }

    @PostMapping
    public ResponseEntity<WorkbookResponse> createWorkbookByUser(@RequestBody @Valid WorkbookRequest workbookRequest,
                                                                 @AuthenticationPrincipal AppUser appUser) {
        WorkbookResponse workbookResponse = workbookService.createWorkbookByUser(workbookRequest, appUser);
        return ResponseEntity.created(URI.create("/api/workbooks/" + workbookResponse.getId())).body(workbookResponse);
    }

    @GetMapping
    public ResponseEntity<List<WorkbookResponse>> findWorkbooksByUser(@AuthenticationPrincipal AppUser appUser) {
        return ResponseEntity.ok(
                workbookService.findWorkbooksByUser(appUser)
        );
    }

    @GetMapping("/public")
    public ResponseEntity<List<WorkbookResponse>> findPublicWorkbooksBySearch(
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(
                workbookService.findPublicWorkbooksBySearch(search)
        );
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<WorkbookCardResponse> findWorkbookCardsById(@PathVariable Long id) {
        return ResponseEntity.ok(
                workbookService.findWorkbookCardsById(id)
        );
    }
}
