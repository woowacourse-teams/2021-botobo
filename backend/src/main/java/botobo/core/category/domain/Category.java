package botobo.core.category.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private boolean isDelete;

    @Column(length = 100)
    private String logoUrl="";

    @Column
    private String description="";

    @Builder
    public Category(Long id, String name, boolean isDelete, String logoUrl,
                    String description) {
        this.id = id;
        this.name = name;
        this.isDelete = isDelete;
        this.logoUrl = logoUrl;
        this.description = description;
    }
}

