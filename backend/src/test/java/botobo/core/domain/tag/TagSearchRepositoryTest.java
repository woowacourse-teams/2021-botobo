package botobo.core.domain.tag;

import botobo.core.config.QuerydslConfig;
import botobo.core.domain.SearchRepositoryTest;
import botobo.core.domain.FilterRepositoryTest;
import botobo.core.domain.tag.dto.TagDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TagSearchRepository.class, QuerydslConfig.class})
class TagSearchRepositoryTest extends SearchRepositoryTest {

    @Autowired
    private TagSearchRepository tagSearchRepository;

    @DisplayName("문제집명을 포함하고 태그명이 문제집명과 일치하는 문제집의 태그를 모두 가져온다. - 성공")
    @Test
    void findAllByWorkbookName() {
        // given, when
        List<Tag> tags = tagSearchRepository.findAllByContainsWorkbookName("Java");

        // then
        assertThat(tags).hasSize(3);
        assertThat(tags)
                .extracting(Tag::getTagNameValue)
                .contains("java", "joanne", "javascript");
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 문제집의 카드가 0개일 때는 가져오지 않는다.")
    @Test
    void findAllByWorkbookNameWithCardZero() {
        // given, when
        List<Tag> tags = tagSearchRepository.findAllByContainsWorkbookName("카드가 없다");

        // then
        assertThat(tags).isEmpty();
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 비공개 문제집은 가져오지 않는다.")
    @Test
    void findAllByWorkbookNamePrivateWorkbook() {
        // given, when
        List<Tag> tags = tagSearchRepository.findAllByContainsWorkbookName("비공개");

        // then
        assertThat(tags).isEmpty();
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 성공, 일치하는 문제집 없음")
    @Test
    void findAllByWorkbookNameEmpty() {
        // given, when
        List<Tag> tags = tagSearchRepository.findAllByContainsWorkbookName("babo");
        // then
        assertThat(tags).isEmpty();
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 성공, 문제집 명이 null일 때 빈 리스트를 반환한다.")
    @Test
    void findAllByWorkbookNameNull() {
        // given, when
        List<Tag> tags = tagSearchRepository.findAllByContainsWorkbookName(null);
        // then
        assertThat(tags).isEmpty();
    }

    @Test
    @DisplayName("keyword에 포함된 태그 조회 - 성공")
    void findAllTagsIn() {
        // when
        List<Tag> tags = tagSearchRepository.findAllTagContaining("Java");
        List<TagDto> tags = tagSearchRepository.findAllTagContaining("자바");

        // then
        assertThat(tags).hasSize(2);
    }

    @Test
    @DisplayName("keyword에 포함된 태그 조회 - 성공, 비공개 문제집에 포함된 태그는 포함되지 않는다.")
    void findAllTagsWhenIncludeInPrivateWorkbook() {
        // when
        List<TagDto> tags = tagSearchRepository.findAllTagContaining("private");

        // then
        assertThat(tags).isEmpty();
    }

    @Test
    @DisplayName("keyword에 포함된 태그 조회 - 성공, 카드가 0개인 문제집은 포함되지 않는다.")
    void findAllTagsWhenEmptyCards() {
        // when
        List<TagDto> tags = tagSearchRepository.findAllTagContaining("empty");

        // then
        assertThat(tags).isEmpty();
    }

    @Test
    @DisplayName("keyword에 포함된 태그 조회 - keyword가 null인 경우 빈 리스트를 응답한다.")
    void findAllTagsContainingWhenKeywordIsNull() {
        // when
        List<TagDto> tags = tagSearchRepository.findAllTagContaining(null);

        // then
        assertThat(tags).isEmpty();
    }
}
