package botobo.core.category.ui;

import botobo.core.category.application.CategoryService;
import botobo.core.category.dto.CategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAllCategories() {
        List<CategoryResponse> categoryResponses = categoryService.findAll();

        return ResponseEntity.ok(categoryResponses);
    }
}
