package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagNames;
import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.tag.Tags;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
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
        Tags tags = tagService.convertTags(tagNames);

        // then
        assertThat(tags.toList()).extracting("tagName")
                .extracting("value")
                .containsExactly("javascript", "java", "spring", "react", "python");
        assertThat(tags.toList())
                .extracting("id")
                .containsExactly(null, java.getId(), spring.getId(), react.getId(), null);
    }
}