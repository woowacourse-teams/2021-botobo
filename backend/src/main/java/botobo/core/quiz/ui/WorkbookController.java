package botobo.core.quiz.ui;

import botobo.core.auth.ui.AuthenticationPrincipal;
import botobo.core.quiz.application.WorkbookService;
import botobo.core.quiz.domain.workbook.WorkbookCriteria;
import botobo.core.quiz.domain.workbook.WorkbookSearchParams;
import botobo.core.quiz.dto.workbook.WorkbookCardResponse;
import botobo.core.quiz.dto.workbook.WorkbookResponse;
import botobo.core.user.domain.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/workbooks")
public class WorkbookController {

    private final WorkbookService workbookService;

    public WorkbookController(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }

    @GetMapping
    public ResponseEntity<List<WorkbookResponse>> findWorkbookByCriteria(
            @WorkbookSearchParams WorkbookCriteria workbookCriteria, @AuthenticationPrincipal AppUser appUser) {

        return ResponseEntity.ok(
                workbookService.searchByCriteria(workbookCriteria, appUser.getId())
        );
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<WorkbookCardResponse> findWorkbookCardsById(@PathVariable Long id) {
        return ResponseEntity.ok(
                workbookService.findWorkbookCardsById(id)
        );
    }
}
