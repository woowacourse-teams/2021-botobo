package botobo.core.domain.card;

import botobo.core.domain.user.User;
import botobo.core.domain.workbook.Workbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardTest {

    @Test
    @DisplayName("Builder를 이용한 Card 객체 생성 - 성공")
    void createWithBuilder() {
        // given
        Workbook workbook = Workbook.builder()
                .name("완벽한 자바 문제집")
                .deleted(false)
                .build();

        // when, then
        assertThatCode(() -> Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(workbook)
                .deleted(false)
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("question이 null일 때 Card 객체 생성 - 실패")
    void createWithNullQuestion() {
        // given
        Workbook workbook = Workbook.builder()
                .name("또 다른 완벽한 자바 문제집")
                .deleted(false)
                .build();

        // when, then
        assertThatThrownBy(() -> Card.builder()
                .id(1L)
                .question(null)
                .answer("답변")
                .workbook(workbook)
                .deleted(false)
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
                .deleted(false)
                .build();

        // when, then
        assertThatThrownBy(() -> Card.builder()
                .id(1L)
                .question("질문")
                .answer(null)
                .workbook(workbook)
                .deleted(false)
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
                .deleted(false)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Card의 Workbook에는 null이 들어갈 수 없습니다.");
    }

    @Test
    @DisplayName("카드 수정 - 성공")
    void update() {
        // given
        final Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(Workbook.temporaryWorkbook())
                .build();

        final Card newCard = Card.builder()
                .question("변경된 질문")
                .answer("변경된 답변")
                .workbook(Workbook.temporaryWorkbook())
                .build();

        // when
        card.update(newCard);

        // then
        assertThat(card.getQuestion()).isEqualTo("변경된 질문");
        assertThat(card.getAnswer()).isEqualTo("변경된 답변");
    }

    @Test
    @DisplayName("카드 삭제 - 성공")
    void delete() {
        // given
        final Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(Workbook.temporaryWorkbook())
                .build();

        // when
        card.delete();

        // then
        assertThat(card.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("마주친 횟수 증가 - 성공")
    void incrementEncounterCount() {
        // given
        final Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(Workbook.temporaryWorkbook())
                .build();

        // when
        card.incrementEncounterCount();

        // then
        assertThat(card.getEncounterCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("다음에 또보기로 추가 - 성공")
    void makeNextQuiz() {
        // given
        final Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(Workbook.temporaryWorkbook())
                .build();

        // when
        card.makeNextQuiz();

        // then
        assertThat(card.isNextQuiz()).isTrue();
    }

    @Test
    @DisplayName("다음에 또보기로 취소 - 성공")
    void cancelNextQuiz() {
        // given
        final Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .nextQuiz(true)
                .workbook(Workbook.temporaryWorkbook())
                .build();

        // when
        card.cancelNextQuiz();

        // then
        assertThat(card.isNextQuiz()).isFalse();
    }

    @Test
    @DisplayName("카드의 작성자인지 확인한다. - 성공")
    void isOwnerOf() {
        // given
        final User user = User.builder()
                .id(1L)
                .userName("joanne")
                .build();

        final Workbook workbook = Workbook.builder()
                .id(1L)
                .name("조앤의 워크북")
                .user(user)
                .build();

        final Card card = Card.builder()
                .id(1L)
                .question("질문")
                .answer("답변")
                .workbook(workbook)
                .build();

        // when, then
        assertThat(card.isAuthorOf(user)).isTrue();
    }
}