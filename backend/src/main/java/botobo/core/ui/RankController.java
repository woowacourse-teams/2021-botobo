package botobo.core.ui;

import botobo.core.application.WorkbookRankService;
import botobo.core.application.rank.SearchRankService;
import botobo.core.dto.rank.SearchRankResponse;
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
    private final SearchRankService searchRankService;

    public RankController(WorkbookRankService workbookRankService, SearchRankService searchRankService) {
        this.workbookRankService = workbookRankService;
        this.searchRankService = searchRankService;
    }

    @GetMapping("/workbooks")
    public ResponseEntity<List<WorkbookResponse>> findWorkbookRanks() {
        return ResponseEntity.ok(
                workbookRankService.findWorkbookRanks()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchRankResponse>> findSearchKeywordRanks() {
        return ResponseEntity.ok(
                searchRankService.bringSearchRanks()
        );
    }
}
