package botobo.core.domain.tag;

import botobo.core.dto.tag.TagResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class FilterTag implements Serializable {

    private Long id;
    private String name;

    public FilterTag(Tag tag) {
        this(tag.getId(), tag.getTagNameValue());
    }

    public FilterTag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagResponse toTagResponse() {
        return TagResponse.builder()
                .id(id)
                .name(name)
                .build();
    }
}
