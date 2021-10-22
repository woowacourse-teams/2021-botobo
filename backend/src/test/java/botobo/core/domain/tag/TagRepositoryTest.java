package botobo.core.domain.tag;

import botobo.core.domain.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagRepositoryTest extends RepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Tag 저장 - 성공")
    void save() {
        // given
        Tag tag = Tag.of("자바");

        // when
        Tag savedTag = tagRepository.save(tag);

        // then
        assertThat(savedTag).isSameAs(tag);
        assertThat(savedTag.getId()).isNotNull();
        assertThat(savedTag.getTagName()).isEqualTo(TagName.of("자바"));
        assertThat(savedTag.getCreatedAt()).isNotNull();
        assertThat(savedTag.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Tag 저장 - 실패, 존재하는 이름")
    void saveWithExistentName() {
        // given
        Tag tag = Tag.of("자바");
        tagRepository.save(tag);
        flushAndClear();

        // when, then
        assertThatThrownBy(() -> tagRepository.save(Tag.of("자바")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("TagName으로 조회 - 성공")
    void findByTagName() {
        // given
        Tag savedTag = tagRepository.save(Tag.of("자바"));
        flushAndClear();

        // when
        Optional<Tag> tag = tagRepository.findByTagName(TagName.of("자바"));

        // then
        assertThat(tag).isNotEmpty();
        assertThat(tag).get().isEqualTo(savedTag);
    }

    public void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
