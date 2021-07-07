package botobo.core.category.application;

import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import botobo.core.category.dto.CategoryDto;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<CategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAll();

        return CategoryDto.listOf(categories);
    }
}
