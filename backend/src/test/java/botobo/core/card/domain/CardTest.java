package botobo.core.card.domain;

import botobo.core.category.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardTest {

    @Test
    @DisplayName("Builder를 이용한 Card 객체 생성 - 성공")
    void createWithBuilder() {
        // given
        Category java = Category.builder()
                .name("java")
                .isDeleted(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        // when, then
        assertThatCode(() -> Card.builder()
                .id(1L)
                .question("질문")
                .category(java)
                .isDeleted(false)
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Question이 null일 때 Card 객체 생성 - 실패")
    void createWithNullQuestion() {
        // given
        Category java = Category.builder()
                .name("java")
                .isDeleted(false)
                .logoUrl("botobo.io")
                .description("~")
                .build();

        // when, then
        assertThatThrownBy(() -> Card.builder()
                .id(1L)
                .question(null)
                .category(java)
                .isDeleted(false)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Category가 null일 때 Card 객체 생성 - 실패")
    void createWithNullCard() {
        // when, then
        assertThatThrownBy(() -> Card.builder()
                .id(1L)
                .question("질문")
                .category(null)
                .isDeleted(false)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }
}