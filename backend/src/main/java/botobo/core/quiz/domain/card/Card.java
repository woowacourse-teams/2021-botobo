package botobo.core.quiz.domain.card;

import botobo.core.quiz.domain.BaseEntity;
import botobo.core.quiz.domain.workbook.Workbook;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class Card extends BaseEntity {

    @Lob
    @Column(nullable = false)
    private String question;

    @Lob
    @Column(nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false)
    private Workbook workbook;

    @Column(nullable = false)
    private int encounterCount;

    @Column(nullable = false)
    private boolean isNextQuiz;

    @Column(nullable = false)
    private boolean isBookmark;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public Card(Long id, String question, String answer, Workbook workbook, int encounterCount, boolean isNextQuiz, boolean isBookmark, boolean isDeleted) {
        validateNull(question, workbook);
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.encounterCount = encounterCount;
        this.isNextQuiz = isNextQuiz;
        this.isBookmark = isBookmark;
        this.isDeleted = isDeleted;
        changeWorkbook(workbook);
    }

    private void validateNull(String question, Workbook workbook) {
        if (Objects.isNull(question)) {
            throw new IllegalArgumentException("Card의 Question에는 null이 들어갈 수 없습니다.");
        }
        if (Objects.isNull(workbook)) {
            throw new IllegalArgumentException("Card의 Workbook에는 null이 들어갈 수 없습니다.");
        }
    }

    public void changeWorkbook(Workbook workbook) {
        if (Objects.nonNull(workbook)) {
            workbook.getCards().removeCard(this);
        }
        this.workbook = workbook;
        workbook.getCards().addCard(this);
    }

    public void incrementEncounterCount() {
        encounterCount++;
    }

    public void makeNextQuizCard() {
        isNextQuiz = true;
    }

    public void cancelNextQuizCard() {
        isNextQuiz = false;
    }
}
