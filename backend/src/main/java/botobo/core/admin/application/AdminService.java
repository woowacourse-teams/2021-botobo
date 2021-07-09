package botobo.core.admin.application;

import botobo.core.admin.dto.AdminCategoryRequest;
import botobo.core.admin.dto.AdminCategoryResponse;
import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final CategoryRepository categoryRepository;

    public AdminService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public AdminCategoryResponse createCategory(AdminCategoryRequest adminCategoryRequest) {
        Category category = adminCategoryRequest.toCategory();
        Category savedCategory = categoryRepository.save(category);
        return AdminCategoryResponse.of(savedCategory);
    }
}
