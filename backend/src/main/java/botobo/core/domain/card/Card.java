package botobo.core.domain.card;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.user.User;
import botobo.core.domain.workbook.Workbook;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
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
@SQLDelete(sql = "UPDATE card SET deleted = true WHERE id = ?") // TODO 이 두 어노테이션에 대해서 알아보기
@Where(clause = "deleted = false")
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

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean nextQuiz;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean bookmark;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean deleted;

    @Builder
    private Card(Long id, String question, String answer, Workbook workbook, int encounterCount, boolean nextQuiz, boolean bookmark, boolean deleted) {
        validateNull(question, answer, workbook);
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.encounterCount = encounterCount;
        this.nextQuiz = nextQuiz;
        this.bookmark = bookmark;
        this.deleted = deleted;
        changeWorkbook(workbook);
    }

    private void validateNull(String question, String answer, Workbook workbook) {
        if (Objects.isNull(question)) {
            throw new IllegalArgumentException("Card의 question에는 null이 들어갈 수 없습니다.");
        }
        if (Objects.isNull(answer)) {
            throw new IllegalArgumentException("Card의 answer에는 null이 들어갈 수 없습니다.");
        }
        if (Objects.isNull(workbook)) {
            throw new IllegalArgumentException("Card의 Workbook에는 null이 들어갈 수 없습니다.");
        }
    }

    public void changeWorkbook(Workbook workbook) {
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

    public void updateFrom(Card other) {
        this.question = other.question;
        this.answer = other.answer;
        this.bookmark = other.bookmark;
        this.nextQuiz = other.nextQuiz;
        changeWorkbook(workbook);
    }

    public boolean equalsNextQuizWith(boolean isNextQuiz) {
        return this.nextQuiz == isNextQuiz;
    }

    public boolean hasSameUser(User user) {
        return workbook.hasSameUser(user);
    }
}
