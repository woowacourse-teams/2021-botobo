package botobo.core.card.domain;

import botobo.core.category.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "card")
    private List<Answer> answers = new ArrayList<>();

    @Builder
    public Card(Long id, String question, boolean isDeleted, Category category, List<Answer> answers) {
        this.id = id;
        this.question = question;
        this.isDeleted = isDeleted;
        setCategory(category);
        this.answers = answers;
    }

    private void setCategory(Category category) {
        if (Objects.nonNull(category)) {
            category.getCards().remove(this);
        }
        this.category = category;
        category.getCards().add(this);
    }
}
