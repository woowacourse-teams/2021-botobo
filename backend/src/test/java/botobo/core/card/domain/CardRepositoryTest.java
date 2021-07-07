package botobo.core.card.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        testEntityManager.flush();
    }

    @Test
    @DisplayName("Card 저장 - 성공")
    void save() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDelete(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();
        categoryRepository.save(category);

        Card card = Card.builder()
                .question("질문")
                .category(category)
                .build();

        // when
        Card savedCard = cardRepository.save(card);

        // then
        assertThat(savedCard.getId()).isNotNull();
    }

    @Test
    @DisplayName("Card 저장 - 실패, 카테고리 없음")
    void saveWithNullCategory() {
        // given
        Card card = Card.builder()
                .question("질문")
                .category(null)
                .build();

//        cardRepository.save(card);

        // when, then
//        assertThatThrownBy(() -> cardRepository.save(card))
//                .isInstanceOf(Null.class);
    }

    @Test
    @DisplayName("Card 저장 - 실패, 질문 없음")
    void saveWithNullQuestion() {
        // given
        Category category = Category.builder()
                .name("java")
                .isDelete(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();
        categoryRepository.save(category);

        Card card = Card.builder()
                .question(null)
                .category(category)
                .build();

        // when, then
//        assertThatThrownBy(() -> cardRepository.save(card))
//                .isInstanceOf();
    }

    @Test
    @DisplayName("Id로 Card 찾기 - 성공")
    void findById() {
        // given
        Card card = Card.builder()
                .question("질문")
                .category(new Category())
                .build();

        // when
        Card savedCard = cardRepository.save(card);

        // when
        Optional<Card> findCard = cardRepository.findById(savedCard.getId());
        assertThat(findCard).containsSame(savedCard);
    }


}