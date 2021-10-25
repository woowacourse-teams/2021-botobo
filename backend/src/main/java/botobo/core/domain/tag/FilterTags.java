package botobo.core.domain.tag;

import botobo.core.dto.tag.TagResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@RedisHash(value = "filterTags", timeToLive = 30L)
public class FilterTags implements Serializable {

    // TODO 5L 변경
    @Id
    private String id;
    private List<FilterTag> filterTags;

    public static FilterTags of (String keyword, List<Tag> tags) {
        List<FilterTag> filterTags = convertToFilterTag(tags);
        return new FilterTags(keyword, filterTags);
    }

    private static List<FilterTag> convertToFilterTag(List<Tag> tags) {
        return tags.stream()
                .map(FilterTag::new)
                .collect(Collectors.toList());
    }

    private FilterTags(String id, List<FilterTag> filterTags) {
        this.id = id;
        this.filterTags = filterTags;
    }

    public List<TagResponse> toTagResponses() {
        return filterTags.stream()
                .map(FilterTag::toTagResponse)
                .collect(Collectors.toList());
    }
}
