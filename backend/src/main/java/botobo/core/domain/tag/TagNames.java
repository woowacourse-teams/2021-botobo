package botobo.core.domain.tag;

import botobo.core.exception.tag.TagNamesCreationFailureException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TagNames {

    private final List<TagName> tagNames;

    private TagNames(List<TagName> tagNames) {
        validateTagNames(tagNames);
        this.tagNames = distinctTagNames(tagNames);
    }

    private void validateTagNames(List<TagName> tagNames) {
        if (Objects.isNull(tagNames)) {
            throw new TagNamesCreationFailureException("tagNames 인자는 null이 될 수 없습니다");
        }
    }

    private List<TagName> distinctTagNames(List<TagName> names) {
        return names.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public static TagNames from(List<String> names) {
        validateNames(names);
        return new TagNames(names.stream()
                .map(TagName::from)
                .collect(Collectors.toList()));
    }

    private static void validateNames(List<String> tagNames) {
        if (Objects.isNull(tagNames)) {
            throw new TagNamesCreationFailureException("names 인자는 null이 될 수 없습니다");
        }
    }

    public List<TagName> toList() {
        return Collections.unmodifiableList(tagNames);
    }
}