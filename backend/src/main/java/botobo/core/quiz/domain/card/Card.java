package botobo.core.quiz.domain.card;

import botobo.core.quiz.domain.BaseEntity;
import botobo.core.quiz.domain.answer.Answers;
import botobo.core.quiz.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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

    @Column(nullable = false)
    private int encounterCount;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Embedded
    private Answers answers = new Answers();

    @Builder
    public Card(Long id, String question, boolean isDeleted, Category category) {
        validateNull(question, category);
        this.id = id;
        this.question = question;
        this.isDeleted = isDeleted;
        setCategory(category);
    }

    private void validateNull(String question, Category category) {
        if (Objects.isNull(question)) {
            throw new IllegalArgumentException("Card의 Question에는 null이 들어갈 수 없습니다.");
        }
        if (Objects.isNull(category)) {
            throw new IllegalArgumentException("Card의 Category에는 null이 들어갈 수 없습니다.");
        }
    }

    private void setCategory(Category category) {
        if (Objects.nonNull(category)) {
            category.getCards().removeCard(this);
        }
        this.category = category;
        category.getCards().addCard(this);
    }

    public String getFirstAnswerContent() {
        return answers.getFirstAnswerContent();
    }

    public void incrementEncounterCount() {
        encounterCount++;
    }
}
