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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    public static String longStringGenerator(int max) {
        return IntStream.rangeClosed(1, max)
                .mapToObj(i -> "x")
                .collect(Collectors.joining());
    }

    @Test
    @DisplayName("Category를 저장 - 성공")
    void save() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDeleted(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);

        // then
        assertThat(category.getId()).isNotNull();
        assertThat(savedCategory).extracting("id").isNotNull();
        assertThat(savedCategory).isSameAs(category);
        testEntityManager.flush();
    }

    @Test
    @DisplayName("동일한 이름으로 Category 저장 - 실패, 이름이 이미 존재")
    void saveWithExistentName() {
        // given
        Category category = Category.builder()
                .name("card")
                .isDeleted(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        categoryRepository.save(category);

        Category duplicatedNameCategory = Category.builder()
                .name("card")
                .isDeleted(false)
                .logoUrl("botobo.io.io")
                .description("~")
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
                .logoUrl("botobo.io")
                .description("~")
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
                .logoUrl("")
                .description("~")
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
    @DisplayName("Category를 생성 - 실패, logoUrl은 최대 100의 길이를 갖는다.")
    void saveWithLongLogoUrl() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDeleted(false)
                .logoUrl(longStringGenerator(101))
                .description("~")
                .build();

        // when, then
        assertThatThrownBy(() -> categoryRepository.save(category))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Category를 id로 조회 - 성공")
    void findById() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDeleted(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();
        Category savedCategory = categoryRepository.save(category);

        // when, then
        Optional<Category> findCategory = categoryRepository.findById(savedCategory.getId());
        assertThat(findCategory).containsSame(savedCategory);
    }
}