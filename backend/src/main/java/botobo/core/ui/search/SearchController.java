package botobo.core.ui.search;

import botobo.core.application.SearchService;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.workbook.WorkbookResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/workbooks")
    public ResponseEntity<List<WorkbookResponse>> searchWorkbooks(@SearchParams WorkbookSearchParameter workbookSearchParameter,
                                                                  @RequestParam(required = false) List<Long> tags,
                                                                  @RequestParam(required = false) List<Long> users) {
        List<WorkbookResponse> workbookResponses = searchService.searchWorkbooks(workbookSearchParameter, tags, users);
        return ResponseEntity.ok(workbookResponses);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagResponse>> recommendRelatedTags(@RequestParam SearchRelated keyword) {
        List<TagResponse> tagResponses = searchService.findTagsIn(keyword);
        return ResponseEntity.ok(tagResponses);
    }
}
