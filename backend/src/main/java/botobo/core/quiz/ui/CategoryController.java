package botobo.core.quiz.ui;

import botobo.core.quiz.application.CategoryService;
import botobo.core.quiz.dto.CategoryResponse;
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
