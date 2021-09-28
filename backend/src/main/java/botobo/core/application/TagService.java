package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagName;
import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.workbooktag.WorkbookTag;
import botobo.core.dto.tag.FilterCriteria;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.tag.TagResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
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
        if (filterCriteria.isEmpty()) {
            return TagResponse.listOf(Tags.empty());
        }
        List<Tag> allTags = tagRepository.findAllByContainsWorkbookName(filterCriteria.getWorkbook());

        Set<Tag> tagSet = new LinkedHashSet<>();
        for (Tag tag : allTags) {
            final List<WorkbookTag> workbookTags = tag.getWorkbookTags();
            filterTagHavingNonZeroCards(tagSet, tag, workbookTags);
        }

        Tags tags = Tags.of(new ArrayList<>(tagSet));
        return TagResponse.listOf(tags);
    }

    private void filterTagHavingNonZeroCards(Set<Tag> tagSet, Tag tag, List<WorkbookTag> workbookTags) {
        workbookTags.stream()
                .filter(workbookTag -> workbookTag.getWorkbook().cardCount() > 0)
                .map(workbookTag -> tag)
                .forEach(tagSet::add);
    }
}
