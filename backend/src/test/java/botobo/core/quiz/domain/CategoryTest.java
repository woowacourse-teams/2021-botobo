package botobo.core.quiz.domain;

import botobo.core.quiz.domain.category.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryTest {

    @Test
    @DisplayName("Builder를 이용한 Category 객체 생성 - 성공")
    void createWithBuilder() {
        // when, then
        assertThatCode(() ->
                Category.builder()
                        .name("java")
                        .isDeleted(false)
                        .logoUrl("botobo.io")
                        .description("~")
                        .build())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Builder를 이용한 카테고리 생성 - 실패, name은 null이 될 수 없다.")
    void createWithNullName() {
        // given
        assertThatThrownBy(() -> Category.builder()
                .name(null)
                .isDeleted(false)
                .logoUrl("botobo.io")
                .description("~")
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Builder를 이용한 카테고리 생성 - 실패, logoUrl은 null이 될 수 없다.")
    void createWithNullLogoUrl() {
        // given
        assertThatThrownBy(() -> Category.builder()
                .name("js")
                .isDeleted(false)
                .logoUrl(null)
                .description("~")
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Builder를 이용한 카테고리 생성 - 실패, description은 null이 될 수 없다.")
    void createWithNullDescription() {
        // given
        assertThatThrownBy(() -> Category.builder()
                .name("js")
                .isDeleted(false)
                .logoUrl("botobo.io")
                .description(null)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}