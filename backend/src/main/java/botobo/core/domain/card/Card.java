package botobo.core.domain.card;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.user.User;
import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.card.CardAnswerNullException;
import botobo.core.exception.card.CardQuestionNullException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

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
@Where(clause = "deleted = false")
public class Card extends BaseEntity {

    @Lob
    @Column(nullable = false)
    private String question;

    @Lob
    @Column(nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id")
    private Workbook workbook;

    @Column(nullable = false)
    private int encounterCount;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean nextQuiz;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean bookmark;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean deleted;

    @Builder
    private Card(Long id, String question, String answer, Workbook workbook, int encounterCount, boolean nextQuiz, boolean bookmark, boolean deleted) {
        validateNull(question, answer);
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.encounterCount = encounterCount;
        this.nextQuiz = nextQuiz;
        this.bookmark = bookmark;
        this.deleted = deleted;
        if (Objects.nonNull(workbook)) {
            changeWorkbook(workbook);
        }
    }

    public static Card createCopyOf(Card other) {
        return Card.builder()
                .question(other.question)
                .answer(other.answer)
                .build();
    }

    private void validateNull(String question, String answer) {
        if (Objects.isNull(question)) {
            throw new CardQuestionNullException();
        }
        if (Objects.isNull(answer)) {
            throw new CardAnswerNullException();
        }
    }

    public void addWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public void update(Card other) {
        this.question = other.question;
        this.answer = other.answer;
        this.bookmark = other.bookmark;
        this.nextQuiz = other.nextQuiz;
    }

    public void delete() {
        this.deleted = true;
    }

    private void changeWorkbook(Workbook workbook) {
        if (Objects.nonNull(this.workbook)) {
            this.workbook.getCards().removeCard(this);
        }
        this.workbook = workbook;
        workbook.getCards().addCard(this);
    }

    public void incrementEncounterCount() {
        encounterCount++;
    }

    public void makeNextQuiz() {
        nextQuiz = true;
    }

    public void cancelNextQuiz() {
        nextQuiz = false;
    }

    public boolean equalsNextQuizWith(boolean isNextQuiz) {
        return this.nextQuiz == isNextQuiz;
    }

    public boolean isAuthorOf(User user) {
        return workbook.isAuthorOf(user);
    }
}
