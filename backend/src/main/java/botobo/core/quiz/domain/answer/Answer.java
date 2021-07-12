package botobo.core.quiz.domain.answer;

import botobo.core.quiz.domain.card.Card;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content = "";

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Builder
    public Answer(Long id, String content, boolean isDeleted, Card card) {
        validateNull(content, card);
        this.id = id;
        this.content = content;
        this.isDeleted = isDeleted;
        setCard(card);
    }

    private void validateNull(String content, Card card) {
        if (Objects.isNull(content)) {
            throw new IllegalArgumentException("Answer의 Content는 null이 될 수 없습니다.");
        }
        if (Objects.isNull(card)) {
            throw new IllegalArgumentException("Answer의 card는 null이 될 수 없습니다.");
        }
    }

    private void setCard(Card card) {
        if (Objects.nonNull(card)) {
            card.getAnswers().removeAnswer(this);
        }
        this.card = card;
        card.getAnswers().addAnswer(this);
    }

}
