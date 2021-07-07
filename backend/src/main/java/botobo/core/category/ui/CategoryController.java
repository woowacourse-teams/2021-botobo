package botobo.core.category.ui;

import botobo.core.category.application.CategoryService;
import botobo.core.category.dto.CategoryDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAllCategories() {
        List<CategoryDto> categoryDtos = categoryService.findAll();

        return ResponseEntity.ok(categoryDtos);
    }
}
