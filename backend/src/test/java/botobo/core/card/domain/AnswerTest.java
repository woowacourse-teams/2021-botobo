package botobo.core.card.domain;

import botobo.core.category.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

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
    @DisplayName("파라미터 없는 생성자로 객체 생성 - 생성")
    void createWithNoArgsConstructor() {
        // when, then
        assertThatCode(Answer::new)
                .doesNotThrowAnyException();
    }
}