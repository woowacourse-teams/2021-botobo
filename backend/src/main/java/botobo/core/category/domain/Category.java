package botobo.core.category.domain;

import botobo.core.card.domain.Card;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false, length = 100)
    private String logoUrl = "";

    @Column(nullable = false)
    private String description = "";

    @OneToMany(mappedBy = "category")
    private List<Card> cards = new ArrayList<>();

    @Builder
    public Category(Long id, String name, boolean isDeleted, String logoUrl,
                    String description) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}

