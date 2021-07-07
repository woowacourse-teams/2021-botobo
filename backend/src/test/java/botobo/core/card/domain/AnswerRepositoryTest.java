package botobo.core.card.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import botobo.core.category.domain.Category;
import botobo.core.category.domain.CategoryRepository;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TestEntityManager testEntityManager;

    private Card card;
    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("java")
                .isDelete(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        card = Card.builder()
                .question("질문")
                .category(category)
                .isDeleted(false)
                .build();

        categoryRepository.save(category);
        cardRepository.save(card);
    }

    @AfterEach
    void tearDown() {
        testEntityManager.flush();
    }

    @Test
    @DisplayName("Answer 저장 - 성공")
    void save() {
        // given
        Answer answer = Answer.builder()
                .content("내용입니다.")
                .card(card)
                .build();

        // when
        Answer savedAnswer = answerRepository.save(answer);

        // then
        assertThat(savedAnswer).extracting("id").isNotNull();
        assertThat(savedAnswer).isSameAs(answer);
    }

    @Test
    @DisplayName("Id로 Answer 찾기 - 성공")
    void findById() {
        // given
        Answer answer = Answer.builder()
                .content("내용입니다.")
                .card(card)
                .build();

        Answer savedAnswer = answerRepository.save(answer);
        Long id = savedAnswer.getId();

        // when, then
        Optional<Answer> findAnswer = answerRepository.findById(id);
        assertThat(findAnswer).isPresent();
        assertThat(findAnswer).containsSame(savedAnswer);
        assertThat(findAnswer).hasValueSatisfying(s -> {
            assertThat(s.getId()).isNotNull();
            assertThat(s.getContent()).isEqualTo("내용입니다.");
        });
    }

    @Test
    @DisplayName("Content로 Answer 찾기 - 성공")
    void findByContent() {
        // given
        Answer answer = Answer.builder()
                .content("내용입니다.")
                .card(card)
                .build();

        Answer savedAnswer = answerRepository.save(answer);
        Long id = savedAnswer.getId();

        // when, then
        Optional<Answer> findAnswer = answerRepository.findByContent("내용입니다.");
        assertThat(findAnswer).isPresent();
        assertThat(findAnswer).containsSame(savedAnswer);
        assertThat(findAnswer).hasValueSatisfying(s -> {
            assertThat(s.getId()).isNotNull();
            assertThat(s.getContent()).isEqualTo("내용입니다.");
        });
    }

    @Test
    @DisplayName("Id로 Answer 찾기 - 실패, 존재하지 않는 Id")
    void findByIdWhenNotExist() {
        //given
        Long id = 100L;

        // when, then
        Optional<Answer> findAnswer = answerRepository.findById(id);
        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("Content에서 null을 넣으면 Default값은 \"\" - 성공")
    void checkContentDefaultValue() {
        // given
        Answer answer = Answer.builder()
                .card(card)
                .build();
        Answer savedAnswer = answerRepository.save(answer);
        Long id = savedAnswer.getId();

        // when
        Optional<Answer> findAnswer = answerRepository.findById(id);

        // then
        assertThat(findAnswer).isPresent();
        assertThat(findAnswer.get().getContent()).isEqualTo("");
    }

    @Test
    @DisplayName("Card에 null을 입력 - 실패, Card는 null이 될 수 없다.")
    void checkCardNull() {
        // given
        Answer answer = Answer.builder()
                .card(null)
                .build();

        // when, then
        answerRepository.save(answer);
    }
}