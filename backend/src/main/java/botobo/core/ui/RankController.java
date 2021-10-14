package botobo.core.ui;

import botobo.core.application.rank.SearchRankService;
import botobo.core.dto.rank.SearchRankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/ranks")
@RestController
public class RankController {

    private final SearchRankService searchRankService;

    public RankController(SearchRankService searchRankService) {
        this.searchRankService = searchRankService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchRankResponse>> findSearchKeywordRanks() {
        return ResponseEntity.ok(
                searchRankService.bringSearchRanks()
        );
    }
}
