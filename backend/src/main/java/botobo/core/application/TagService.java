package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagNames;
import botobo.core.domain.tag.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> convertTags(TagNames tagNames) {
        return tagNames.toList()
                .stream()
                .map(tagName -> tagRepository.findByTagName(tagName).orElse(Tag.from(tagName)))
                .collect(Collectors.toList());
    }
}
