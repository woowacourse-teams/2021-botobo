package botobo.core.quiz.domain;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.card.CardRepository;
import botobo.core.quiz.domain.category.Category;
import botobo.core.quiz.domain.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(savedCard).isSameAs(card);
        assertThat(savedCard.getCreatedAt()).isNotNull();
        assertThat(savedCard.getUpdatedAt()).isNotNull();
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