package botobo.core.card.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
        this.id = id;
        this.content = content;
        this.isDeleted = isDeleted;
        this.card = card;
    }


}
