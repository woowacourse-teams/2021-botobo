package botobo.core.quiz.domain;

import botobo.core.quiz.domain.category.Category;
import botobo.core.quiz.domain.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Category를 저장 - 성공")
    void save() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDeleted(false)
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);

        // then
        assertThat(category.getId()).isNotNull();
        assertThat(savedCategory).extracting("id").isNotNull();
        assertThat(savedCategory).isSameAs(category);
        assertThat(savedCategory.getCreatedAt()).isNotNull();
        assertThat(savedCategory.getUpdatedAt()).isNotNull();
        testEntityManager.flush();
    }

    @Test
    @DisplayName("동일한 이름으로 Category 저장 - 실패, 이름이 이미 존재")
    void saveWithExistentName() {
        // given
        Category category = Category.builder()
                .name("card")
                .isDeleted(false)
                .build();

        categoryRepository.save(category);

        Category duplicatedNameCategory = Category.builder()
                .name("card")
                .isDeleted(false)
                .build();

        // when
        assertThatCode(() -> categoryRepository.save(duplicatedNameCategory))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Category 저장 - 실패, 이름이 최대 길이를 초과")
    void saveWithLongName() {
        // given
        Category category = Category.builder()
                .name("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz")
                .isDeleted(false)
                .build();

        // when, then
        assertThatThrownBy(() -> categoryRepository.save(category))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Category를 생성 - 성공")
    void saveWithLogoUrl() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDeleted(false)
                .build();

        // when, then
        // when
        Category savedCategory = categoryRepository.save(category);

        // then
        assertThat(category.getId()).isNotNull();
        assertThat(savedCategory).extracting("id").isNotNull();
        assertThat(savedCategory).isSameAs(category);
        testEntityManager.flush();
    }


    @Test
    @DisplayName("Category를 id로 조회 - 성공")
    void findById() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDeleted(false)
                .build();
        Category savedCategory = categoryRepository.save(category);

        // when, then
        Optional<Category> findCategory = categoryRepository.findById(savedCategory.getId());
        assertThat(findCategory).containsSame(savedCategory);
    }
}