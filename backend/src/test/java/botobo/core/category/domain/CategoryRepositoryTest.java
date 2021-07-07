package botobo.core.category.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    void tearDown() {
        testEntityManager.flush();
    }

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
        assertThat(savedCategory).extracting("id").isNotNull();
        assertThat(savedCategory).isSameAs(category);
    }

    @Test
    @DisplayName("name이 null이면 비어있는 문자열의 이름으로 생성 - 성공")
    void saveWithNullName() {
        // given
        Category nullNameCategory = Category.builder()
                .name(null)
                .isDelete(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        // when
        Category savedCategory = categoryRepository.save(nullNameCategory);

        // then
        assertThat(savedCategory.getName()).isEqualTo("");
    }

    @Test
    @DisplayName("Category 저장 - 실패, 이름이 이미 존재")
    void saveWithExistentName() {
        // given
        Category category = Category.builder()
                .name("card")
                .isDelete(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        // when, then
        assertThatThrownBy(() -> categoryRepository.save(category))
                .isInstanceOf(DataAccessException.class);
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
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @DisplayName("100이상의 길이를 갖는 logo Url로 Category를 생성 - 실패")
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
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @DisplayName("Description에 update 쿼리로 null을 넣는다.공성")
    void updateDescriptionWithNull() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDelete(false)
                .logoUrl("botobo.io")
                .build();
        Category saveCategory = categoryRepository.save(category);
        Long id = saveCategory.getId();

        // when
        category.updateDescription(null);

        // then
        Optional<Category> findCategory = categoryRepository.findById(id);
        assertThat(findCategory.get().getDescription()).isNotNull();
        assertThat(findCategory.get().getDescription()).isEmpty();
    }

    @Test
    @DisplayName("Category를 id로 조회")
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