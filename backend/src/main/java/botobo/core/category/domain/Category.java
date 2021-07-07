package botobo.core.category.domain;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(columnDefinition = "varchar(100) default ''")
    private String logoUrl;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String description;

    @Builder
    public Category(Long id, String name, boolean isDelete, String logoUrl,
                    String description) {
        this.id = id;
        this.name = name;
        this.isDelete = isDelete;
        this.logoUrl = logoUrl;
        this.description = description;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}

