package botobo.core.card.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("java")
                .isDeleted(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();
        categoryRepository.save(category);
    }

    @Test
    @DisplayName("Card 저장 - 성공")
    void save() {
        // given
        Card card = Card.builder()
                .question("질문")
                .category(category)
                .build();

        // when
        Card savedCard = cardRepository.save(card);
        testEntityManager.flush();

        // then
        assertThat(savedCard.getId()).isNotNull();
    }

    @Test
    @DisplayName("Card 저장 - 실패, 카테고리는 null이 될 수 없다.")
    void saveWithNullCategory() {
        // given
        Card card = Card.builder()
                .question("질문")
                .category(null)
                .build();

        // when, then
        assertThatThrownBy(() -> cardRepository.save(card))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Card 저장 - 실패, question은 null이 될 수 없다.")
    void saveWithNullQuestion() {
        // given
        Card card = Card.builder()
                .question(null)
                .category(category)
                .build();

        // when, then
        assertThatThrownBy(() -> cardRepository.save(card))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Id로 Card 찾기 - 성공")
    void findById() {
        // given
        Card card = Card.builder()
                .question("질문")
                .category(category)
                .build();

        // when
        Card savedCard = cardRepository.save(card);

        // when
        Optional<Card> findCard = cardRepository.findById(savedCard.getId());
        assertThat(findCard).containsSame(savedCard);
    }

    @Test
    @DisplayName("Card 추가 시, 카테고리도 함께 추가 - 성공")
    void checkCategoryIsSaved() {
        // given
        Card card = Card.builder()
                .question("질문")
                .category(category)
                .build();
        final Card savedCard = cardRepository.save(card);

        // when
        final Optional<Card> findCard = cardRepository.findById(savedCard.getId());

        // then
        assertThat(findCard).isPresent();
        assertThat(findCard.get().getCategory()).isNotNull();
    }
}