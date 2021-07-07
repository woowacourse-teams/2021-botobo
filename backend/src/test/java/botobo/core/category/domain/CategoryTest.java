package botobo.core.category.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

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
    @DisplayName("파라미터 없는 생성자로 객체 생성 - 생성")
    void createWithNoArgsConstructor() {
        // when, then
        assertThatCode(Category::new)
                .doesNotThrowAnyException();
    }
}