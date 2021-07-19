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
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}