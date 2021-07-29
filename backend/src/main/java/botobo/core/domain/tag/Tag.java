package botobo.core.domain.tag;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.workbooktag.WorkbookTag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Tag extends BaseEntity {

    @Embedded
    private TagName tagName;

    @OneToMany(mappedBy = "tag")
    private List<WorkbookTag> workbookTags = new ArrayList<>();

    private Tag(TagName tagName) {
        this.tagName = tagName;
    }

    public static Tag from(TagName tagName) {
        return new Tag(tagName);
    }

    public String getTagNameValue() {
        return tagName.getValue();
    }
}
