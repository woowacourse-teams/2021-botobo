package botobo.core.domain.tag;

import botobo.core.config.QuerydslConfig;
import botobo.core.domain.FilterRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TagFilterRepository.class, QuerydslConfig.class})
class TagFilterRepositoryTest extends FilterRepositoryTest {

    @Autowired
    private TagFilterRepository tagFilterRepository;

    @DisplayName("문제집명을 포함하고 태그명이 문제집명과 일치하는 문제집의 태그를 모두 가져온다. - 성공")
    @Test
    void findAllByWorkbookName() {
        // given, when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName("java");

        // then
        assertThat(tags).hasSize(7);
        assertThat(tags)
                .extracting(Tag::getTagNameValue)
                .contains("java", "자바", "자바짱", "jdk", "js", "javascript", "spring");
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 문제집의 카드가 0개일 때는 가져오지 않는다.")
    @Test
    void findAllByWorkbookNameWithCardZero() {
        // given, when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName("카드가 없는");

        // then
        assertThat(tags).hasSize(0);
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 비공개 문제집은 가져오지 않는다.")
    @Test
    void findAllByWorkbookNamePrivateWorkbook() {
        // given, when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName("비공개");

        // then
        assertThat(tags).isEmpty();
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 성공, 일치하는 문제집 없음")
    @Test
    void findAllByWorkbookNameEmpty() {
        // given, when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName("babo");
        // then
        assertThat(tags).isEmpty();
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 성공, 문제집 명이 null일 때 빈 리스트를 반환한다.")
    @Test
    void findAllByWorkbookNameNull() {
        // given, when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName(null);
        // then
        assertThat(tags).isEmpty();
    }
}
