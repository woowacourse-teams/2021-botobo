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
    @Id
    private String id;
    private List<FilterTag> filterTags;

    public static FilterTags of (String keyword, List<TagResponse> tagResponses) {
        List<FilterTag> filterTags = convertToFilterTag(tagResponses);
        return new FilterTags(keyword, filterTags);
    }

    private static List<FilterTag> convertToFilterTag(List<TagResponse> tagResponses) {
        return tagResponses.stream()
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
