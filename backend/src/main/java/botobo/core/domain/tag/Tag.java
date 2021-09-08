package botobo.core.domain.tag;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.workbooktag.WorkbookTag;
import botobo.core.exception.tag.TagNullException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Tag extends BaseEntity {

    @OneToMany(mappedBy = "tag")
    private final List<WorkbookTag> workbookTags = new ArrayList<>();
    @Embedded
    private TagName tagName;

    private Tag(TagName tagName) {
        validateNotNull(tagName);
        this.tagName = tagName;
    }

    public static Tag of(TagName tagName) {
        return new Tag(tagName);
    }

    public static Tag of(String tagNameValue) {
        return new Tag(TagName.of(tagNameValue));
    }

    private void validateNotNull(TagName tagName) {
        if (Objects.isNull(tagName)) {
            throw new TagNullException();
        }
    }

    public String getTagNameValue() {
        return tagName.getValue();
    }
}
