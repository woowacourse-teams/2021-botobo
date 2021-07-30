package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagNames;
import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.tag.Tags;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tags convertTags(TagNames tagNames) {
        return Tags.from(tagNames.toList()
                .stream()
                .map(tagName -> tagRepository.findByTagName(tagName).orElse(Tag.from(tagName)))
                .collect(Collectors.toList()));
    }
}
