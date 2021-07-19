package botobo.core.quiz.domain;

import botobo.core.quiz.domain.answer.Answer;
import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.category.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswerTest {

    @Test
    @DisplayName("Builder를 이용한 Answer 객체 생성 - 성공")
    void createWithBuilder() {
        // given
        Card card = Card.builder()
                .id(1L)
                .question("질문")
                .category(new Category())
                .isDeleted(false)
                .build();

        // when, then
        assertThatCode(() -> Answer.builder()
                .content("정답입니다!")
                .isDeleted(false)
                .card(card)
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Content가 null인 Answer 객체 생성 - 실패")
    void createWithNullContent() {
        // given
        Card card = Card.builder()
                .id(1L)
                .question("질문")
                .category(new Category())
                .isDeleted(false)
                .build();

        // when, then
        assertThatThrownBy(() -> Answer.builder()
                .content(null)
                .isDeleted(false)
                .card(card)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Card가 null인 Answer 객체 생성 - 실패")
    void createWithNullCard() {
        // when, then
        assertThatThrownBy(() -> Answer.builder()
                .content("정답입니다.")
                .isDeleted(false)
                .card(null)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }
}