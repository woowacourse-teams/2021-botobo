package botobo.core.quiz.domain.card;

import botobo.core.quiz.utils.CardFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class QuizTest {

    private List<Card> tenCards;

    @BeforeEach
    void setUp() {
        tenCards = new ArrayList<>(CardFactory.createGuestCards());
    }

    private void makeNextQuiz(int counts) {
        tenCards.stream().limit(counts).forEach(Card::makeNextQuiz);
    }

    private List<Card> getNextQuiz(List<Card> cards) {
        return cards.stream().filter(Card::isNextQuiz).collect(Collectors.toList());
    }

    private List<Integer> getEncounterCounts(List<Card> cards) {
        return cards.stream().map(Card::getEncounterCount).collect(Collectors.toList());
    }

    @DisplayName("isNextQuiz 3개를 포함하여 10개의 카드가 담긴 퀴즈를 생성한다. - 성공")
    @Test
    void makeQuiz() {
        // given
        makeNextQuiz(3);
        List<Card> nextQuiz = getNextQuiz(tenCards);
        Quiz quiz = new Quiz(tenCards, 10);

        // when
        final Cards createdQuiz = quiz.makeQuiz();

        // then
        assertThat(createdQuiz.counts()).isEqualTo(10);
        assertThat(createdQuiz.getCards()).containsAll(nextQuiz);
    }

    @DisplayName("isNextQuiz 0개를 포함하여 10개의 카드가 담긴 퀴즈를 생성한다. - 성공")
    @Test
    void makeQuizWithoutNextQuiz() {
        // given
        Quiz quiz = new Quiz(tenCards, 10);

        // when
        final Cards createdQuiz = quiz.makeQuiz();

        // then
        assertThat(createdQuiz.counts()).isEqualTo(10);
    }

    @DisplayName("isNextQuiz 10개를 포함하여 10개의 카드가 담긴 퀴즈를 생성한다. - 성공")
    @Test
    void makeQuizOnlyWithNextQuiz() {
        // given
        makeNextQuiz(10);
        List<Card> nextQuiz = getNextQuiz(tenCards);
        Quiz quiz = new Quiz(tenCards, 10);

        // when
        final Cards createdQuiz = quiz.makeQuiz();

        // then
        assertThat(createdQuiz.counts()).isEqualTo(10);
        assertThat(createdQuiz.getCards()).containsAll(nextQuiz);
    }

    @DisplayName("다음에 또 보기는 1회 포함 후 취소된다. - 성공")
    @Test
    void cancelNextQuiz() {
        // given
        // 10개의 다음에 또보기 옵션이 추가된 카드인 tenCards로 퀴즈를 생성한다.
        makeNextQuiz(10);
        Quiz quiz = new Quiz(tenCards, 10);
        quiz.makeQuiz();

        // when
        // 이후 tenCards에 대해 NextQuiz 옵션이 포함된 카드를 조회한다.
        final List<Card> afterNextQuizzes = getNextQuiz(tenCards);

        // then
        assertThat(afterNextQuizzes).isEmpty();
    }
}