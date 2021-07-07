package botobo.core.category.domain;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

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
                .isDelete(false)
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
    @DisplayName("name에 null을 넣는다. - 실패, name은 null이 될 수 없다.")
    void saveWithNullName() {
        // given
        Category nullNameCategory = Category.builder()
                .name(null)
                .isDelete(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        // when
        assertThatCode(() -> categoryRepository.save(nullNameCategory))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("동일한 이름으로 Category 저장 - 실패, 이름이 이미 존재")
    void saveWithExistentName() {
        // given
        Category category = Category.builder()
                .name("card")
                .isDelete(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        categoryRepository.save(category);

        Category duplicatedNameCategory = Category.builder()
                .name("card")
                .isDelete(false)
                .logoUrl("botobo.io.io")
                .description("~")
                .build();

        // when
        assertThatCode(() ->categoryRepository.save(duplicatedNameCategory))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Category 저장 - 실패, 이름이 최대 길이를 초과")
    void saveWithLongName() {
        // given
        Category category = Category.builder()
                .name("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz")
                .isDelete(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        // when, then
        assertThatThrownBy(() -> categoryRepository.save(category))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Category를 생성 - 실패, logoUrl은 최대 100의 길이를 갖는다.")
    void saveWithLongLogoUrl() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDelete(false)
                .logoUrl(longStringGenerator())
                .description("~")
                .build();

        // when, then
        assertThatThrownBy(() -> categoryRepository.save(category))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("logoUrl에 null을 입력 - 실패, logoUrl은 null이 될 수 없다. ")
    void saveWithNullLogoUrl() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDelete(false)
                .logoUrl(null)
                .description("~")
                .build();

        // when, then
        assertThatThrownBy(() -> categoryRepository.save(category))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("description에 null을 입력 - 실패, description은 null이 될 수 없다.")
    void updateDescriptionWithNull() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDelete(false)
                .logoUrl("botobo.io")
                .description(null)
                .build();

        assertThatCode(() -> categoryRepository.save(category))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Category를 id로 조회 - 성공")
    void findById() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDelete(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();
        Category savedCategory = categoryRepository.save(category);

        // when, then
        Optional<Category> findCategory = categoryRepository.findById(savedCategory.getId());
        assertThat(findCategory).containsSame(savedCategory);
    }

    private String longStringGenerator() {
        return IntStream.range(1, 110)
                .mapToObj(i -> "x")
                .collect(Collectors.joining());
    }
}