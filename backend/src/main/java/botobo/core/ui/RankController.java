package botobo.core.ui;

import botobo.core.application.WorkbookRankService;
import botobo.core.dto.workbook.WorkbookResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranks")
public class RankController {

    private final WorkbookRankService workbookRankService;

    public RankController(WorkbookRankService workbookRankService) {
        this.workbookRankService = workbookRankService;
    }

    @GetMapping("/workbooks")
    public ResponseEntity<List<WorkbookResponse>> findWorkbookRanks() {
        return ResponseEntity.ok(
                workbookRankService.findWorkbookRanks()
        );
    }
}
