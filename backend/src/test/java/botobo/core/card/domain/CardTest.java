package botobo.core.card.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import botobo.core.category.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @DisplayName("파라미터 없는 생성자로 객체 생성 - 생성")
    void createWithNoArgsConstructor() {
        // when, then
        assertThatCode(Card::new)
                .doesNotThrowAnyException();
    }
}