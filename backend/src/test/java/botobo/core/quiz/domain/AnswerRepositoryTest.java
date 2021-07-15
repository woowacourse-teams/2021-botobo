package botobo.core.quiz.domain;

import botobo.core.quiz.domain.answer.Answer;
import botobo.core.quiz.domain.answer.AnswerRepository;
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
                .isDeleted(false)
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
        testEntityManager.flush();

        // then
        assertThat(savedAnswer).extracting("id").isNotNull();
        assertThat(savedAnswer).isSameAs(answer);
        assertThat(savedAnswer.getCreatedAt()).isNotNull();
        assertThat(savedAnswer.getUpdatedAt()).isNotNull();
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
    @DisplayName("Id로 Answer 찾기 - 실패, 존재하지 않는 Id")
    void findByIdWhenNotExist() {
        //given
        Long id = 100L;

        // when, then
        Optional<Answer> findAnswer = answerRepository.findById(id);
        assertThat(findAnswer).isNotPresent();
    }

    /**
     * 즉시로딩 지연로딩
     */
    @Test
    @DisplayName("Answer 추가 시, Card와 Category가 추가 - 성공")
    void checkCardAndCategoryIsSaved() {
        // given
        Answer answer = Answer.builder()
                .content("내용입니다.")
                .card(card)
                .build();

        answerRepository.save(answer);

        // when
        Optional<Answer> actual = answerRepository.findById(answer.getId());

        // then
        assertThat(actual).isPresent();
        assertThat(actual.get().getCard()).isNotNull();
        assertThat(actual.get().getCard().getCategory()).isNotNull();
    }
}