package botobo.core.domain.tag;

import botobo.core.dto.tag.TagResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.List;

@Getter
@NoArgsConstructor
@RedisHash("tags")
// tags, users
// class 명 어케할

public class FilterTags {

    /**
     * 검색어: java
     * "java" :{
     *     List<tagResponse>
     * }
     */
    @Id
    private String keyword;
    // TagResponse, .. DTO
    private List<FilterTag> tagResponseList;

    /**
     * FilterTag
     * TagResponse랑 같게 생김
     *
     */
}
