package botobo.core.domain.tag;

import botobo.core.exception.tag.TagsCreationFailureException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tags {

    private final List<Tag> tags;

    private Tags(List<Tag> tags) {
        validateNotNull(tags);
        this.tags = tags;
    }

    public static Tags empty() {
        return new Tags(new ArrayList<>());
    }

    public static Tags of(List<Tag> tags) {
        return new Tags(tags);
    }

    private void validateNotNull(List<Tag> tags) {
        if (Objects.isNull(tags)) {
            throw new TagsCreationFailureException("tags는 null이 될 수 없습니다.");
        }
    }

    public int countSameTagName(Tags other) {
        List<String> tagNames = extractTagNames(this.tags);
        List<String> otherNames = extractTagNames(other.tags);
        tagNames.addAll(otherNames);

        Map<String, Long> frequencyTable = tagNames.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return (int) frequencyTable.values()
                .stream()
                .filter(frequency -> frequency > 1)
                .count();
    }

    private List<String> extractTagNames(List<Tag> tags) {
        return tags.stream()
                .map(Tag::getTagNameValue)
                .collect(Collectors.toList());
    }

    public int size() {
        return tags.size();
    }

    public Stream<Tag> stream() {
        return tags.stream();
    }

    public List<Tag> toList() {
        return Collections.unmodifiableList(tags);
    }
}
