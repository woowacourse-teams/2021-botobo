package botobo.core.ui;

import botobo.core.application.TagService;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.tag.FilterCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> findAllTagsByWorkbookName(@RequestParam String workbook) {
        FilterCriteria filterCriteria = new FilterCriteria(workbook);
        List<TagResponse> responses = tagService.findAllTagsByWorkbookName(filterCriteria);
        return ResponseEntity.ok(responses);
    }
}
