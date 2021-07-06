package botobo.core.category.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Category {

    private Long id;
    private String name;
    private boolean isDelete;
    private String logoUrl;
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
}

