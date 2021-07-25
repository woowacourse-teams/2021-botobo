package botobo.core.quiz.domain.card;

import botobo.core.quiz.domain.workbook.Workbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardTest {

    @Test
    @DisplayName("Builder를 이용한 Card 객체 생성 - 성공")
    void createWithBuilder() {
        // given
        Workbook workbook = Workbook.builder()
                .name("완벽한 자바 문제집")
                .isDeleted(false)
                .build();

        // when, then
        assertThatCode(() -> Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(workbook)
                .isDeleted(false)
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("question이 null일 때 Card 객체 생성 - 실패")
    void createWithNullQuestion() {
        // given
        Workbook workbook = Workbook.builder()
                .name("또 다른 완벽한 자바 문제집")
                .isDeleted(false)
                .build();

        // when, then
        assertThatThrownBy(() -> Card.builder()
                .id(1L)
                .question(null)
                .answer("답변")
                .workbook(workbook)
                .isDeleted(false)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Card의 question에는 null이 들어갈 수 없습니다.");
    }

    @Test
    @DisplayName("answer가 null일 때 Card 객체 생성 - 실패")
    void createWithNullAnswer() {
        // given
        Workbook workbook = Workbook.builder()
                .name("또또 다른 완벽한 자바 문제집")
                .isDeleted(false)
                .build();

        // when, then
        assertThatThrownBy(() -> Card.builder()
                .id(1L)
                .question("질문")
                .answer(null)
                .workbook(workbook)
                .isDeleted(false)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Card의 answer에는 null이 들어갈 수 없습니다.");
    }

    @Test
    @DisplayName("Workbook가 null일 때 Card 객체 생성 - 실패")
    void createWithNullCard() {
        // when, then
        assertThatThrownBy(() -> Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(null)
                .isDeleted(false)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Card의 Workbook에는 null이 들어갈 수 없습니다.");
    }
}