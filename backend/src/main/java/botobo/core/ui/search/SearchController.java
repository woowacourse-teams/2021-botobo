package botobo.core.ui.search;

import botobo.core.application.SearchService;
import botobo.core.dto.user.SimpleUserResponse;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/workbooks")
    public ResponseEntity<List<WorkbookResponse>> searchWorkbooks(@SearchParams WorkbookSearchParameter workbookSearchParameter) {
        List<WorkbookResponse> workbookResponses = searchService.searchWorkbooks(workbookSearchParameter);
        return ResponseEntity.ok(workbookResponses);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagResponse>> searchTags(@RequestParam String keyword) {
        SearchKeyword searchKeyword = SearchKeyword.of(keyword);
        List<TagResponse> tagResponses = searchService.searchTags(searchKeyword);
        return ResponseEntity.ok(tagResponses);
    }

    @GetMapping("/users")
    public ResponseEntity<List<SimpleUserResponse>> searchUsers(@RequestParam String keyword) {
        SearchKeyword searchKeyword = SearchKeyword.of(keyword);
        List<SimpleUserResponse> userResponses = searchService.searchUsers(searchKeyword);
        return ResponseEntity.ok(userResponses);
    }
}
