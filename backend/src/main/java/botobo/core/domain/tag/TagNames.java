package botobo.core.domain.tag;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TagNames {

    private final List<TagName> tagNames;

    private TagNames(List<String> names) {
        this.tagNames = distinctTagNames(names);
    }

    public static TagNames from(List<String> names) {
        return new TagNames(names);
    }

    private List<TagName> distinctTagNames(List<String> names) {
        return names.stream()
                .distinct()
                .map(TagName::from)
                .collect(Collectors.toList());
    }

    public List<TagName> toList() {
        return Collections.unmodifiableList(tagNames);
    }
}