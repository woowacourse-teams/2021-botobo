package botobo.core.quiz.application;

import botobo.core.quiz.domain.category.Category;
import botobo.core.quiz.domain.category.CategoryRepository;
import botobo.core.quiz.dto.CategoryCardsResponse;
import botobo.core.quiz.dto.CategoryResponse;
import botobo.core.quiz.exception.CategoryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryResponse.listOf(categories);
    }

    public CategoryCardsResponse findCategoryCardsById(Long id) {
        Category category = categoryRepository.findCategoryAndCardsByIdJoinFetch(id)
                .orElseThrow(CategoryNotFoundException::new);
        return CategoryCardsResponse.of(category);
    }
}
