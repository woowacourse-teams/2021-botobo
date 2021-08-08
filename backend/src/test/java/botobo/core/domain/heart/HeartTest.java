package botobo.core.domain.heart;

import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.heart.HeartCreationFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HeartTest {

    private Workbook workbook;
    private Long userId;

    @BeforeEach
    void setUp() {
        workbook = Workbook.builder()
                .id(1L)
                .name("문제집")
                .build();
        userId = 10L;
    }

    @Test
    @DisplayName("Heart 객체 생성 - 성공")
    void create() {
        // when
        Heart heart = Heart.builder()
                .workbook(workbook)
                .userId(userId)
                .build();

        // then
        assertThat(heart.getWorkbook()).isEqualTo(workbook);
        assertThat(heart.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Heart 객체 생성 - 실패, 문제집 없음")
    void createWithNullWorkbook() {
        // when, then
        assertThatThrownBy(
                () -> Heart.builder()
                        .userId(userId)
                        .build()
        ).isInstanceOf(HeartCreationFailureException.class)
                .hasMessageContaining("Heart 생성에 실패했습니다")
                .hasMessageContaining("문제집 필요");
    }

    @Test
    @DisplayName("Heart 객체 생성 - 실패, 유저 아이디 없음")
    void createWithNullUserId() {
        // when, then
        assertThatThrownBy(
                () -> Heart.builder()
                        .workbook(workbook)
                        .build()
        ).isInstanceOf(HeartCreationFailureException.class)
                .hasMessageContaining("Heart 생성에 실패했습니다")
                .hasMessageContaining("유저 아이디 필요");
    }
}