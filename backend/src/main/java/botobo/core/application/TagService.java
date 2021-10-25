package botobo.core.application;

import botobo.core.domain.tag.TagCacheRepository;
import botobo.core.domain.tag.FilterTags;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagName;
import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.tag.TagSearchRepository;
import botobo.core.domain.tag.Tags;
import botobo.core.dto.tag.FilterCriteria;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.tag.TagResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final TagSearchRepository tagSearchRepository;
    private final TagCacheRepository tagCacheRepository;

    public TagService(TagRepository tagRepository, TagSearchRepository tagSearchRepository, TagCacheRepository tagCacheRepository) {
        this.tagRepository = tagRepository;
        this.tagSearchRepository = tagSearchRepository;
        this.tagCacheRepository = tagCacheRepository;
    }

    public Tags convertTags(List<TagRequest> tagRequests) {
        List<Tag> tags = tagRequests.stream()
                .map(TagRequest::getName)
                .map(TagName::of)
                .distinct()
                .map(tagName -> tagRepository.findByTagName(tagName).orElse(Tag.of(tagName)))
                .collect(Collectors.toList());
        return Tags.of(tags);
    }

    public List<TagResponse> findAllTagsByWorkbookName(FilterCriteria filterCriteria) {
        try {
            if (filterCriteria.isEmpty()) {
                return TagResponse.listOf(Tags.empty());
            }
            return findAllTags(filterCriteria.getWorkbook());
        } catch (RuntimeException e) {
            tagCacheRepository.deleteById(filterCriteria.getWorkbook());
            throw e;
        }
    }

    private List<TagResponse> findAllTags(String keyword) {
        Optional<FilterTags> filterTags = tagCacheRepository.findById(keyword);
        if (filterTags.isPresent()) {
            return filterTags.get().toTagResponses();
        }

        List<Tag> findTags = tagSearchRepository.findAllByContainsWorkbookName(keyword);
        tagCacheRepository.save(FilterTags.of(keyword, findTags));
        return TagResponse.listOf(Tags.of(findTags));
    }

}
