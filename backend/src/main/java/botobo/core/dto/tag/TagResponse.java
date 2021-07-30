package botobo.core.dto.tag;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class TagResponse {

    private Long id;
    private String name;

    public static TagResponse of(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getTagNameValue())
                .build();
    }

    public static List<TagResponse> listOf(Tags tags) {
        return tags.stream()
                .map(TagResponse::of)
                .collect(Collectors.toList());
    }
}
