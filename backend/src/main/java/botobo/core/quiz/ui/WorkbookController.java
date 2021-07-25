package botobo.core.quiz.ui;

import botobo.core.quiz.application.WorkbookService;
import botobo.core.quiz.dto.workbook.WorkbookCardResponse;
import botobo.core.quiz.dto.workbook.WorkbookResponse;
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
    public ResponseEntity<List<WorkbookResponse>> findAllWorkbooks() {
        return ResponseEntity.ok(
                workbookService.findAll()
        );
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<WorkbookCardResponse> findWorkbookCardsById(@PathVariable Long id) {
        return ResponseEntity.ok(
                workbookService.findWorkbookCardsById(id)
        );
    }
}
