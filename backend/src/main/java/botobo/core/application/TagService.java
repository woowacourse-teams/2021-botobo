package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagName;
import botobo.core.domain.tag.TagQueryRepository;
import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.tag.Tags;
import botobo.core.dto.tag.FilterCriteria;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.tag.TagResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final TagQueryRepository tagQueryRepository;

    public TagService(TagRepository tagRepository, TagQueryRepository tagQueryRepository) {
        this.tagRepository = tagRepository;
        this.tagQueryRepository = tagQueryRepository;
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

    @Cacheable(value = "filterTags", key = "#filterCriteria.workbook")
    public List<TagResponse> findAllTagsByWorkbookName(FilterCriteria filterCriteria) {
        String keyword = filterCriteria.getWorkbook();
        if (filterCriteria.isEmpty()) {
            return TagResponse.listOf(Tags.empty());
        }

        List<Tag> findTags = tagQueryRepository.findAllByContainsWorkbookName(keyword);
        return TagResponse.listOf(Tags.of(findTags));
    }

}
