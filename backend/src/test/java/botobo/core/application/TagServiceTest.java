package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagNames;
import botobo.core.domain.tag.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TagServiceTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagService tagService;

    @Test
    @DisplayName("TagNames에서 DB에 이미 존재하는 태그는 기존 태그를 가져오고 존재하지 않는 태그는 새로 생성된다.")
    void convertTags() {
        // given
        Tag java = tagRepository.save(Tag.from("java"));
        Tag spring = tagRepository.save(Tag.from("spring"));
        Tag react = tagRepository.save(Tag.from("react"));
        tagRepository.flush();

        // when
        TagNames tagNames = TagNames.from(
                Arrays.asList("javascript", "java", "spring", "react", "python")
        );
        List<Tag> tags = tagService.convertTags(tagNames);

        // then
        assertThat(tags).extracting("tagName")
                .extracting("value")
                .containsExactly("javascript", "java", "spring", "react", "python");
        assertThat(tags)
                .extracting("id")
                .containsExactly(null, java.getId(), spring.getId(), react.getId(), null);
    }
}