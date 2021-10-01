package botobo.core.domain.tag;

import botobo.core.config.QuerydslConfig;
import botobo.core.domain.card.Card;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Import({TagFilterRepository.class, QuerydslConfig.class})
class TagFilterRepositoryTest {
    @Autowired
    private TagFilterRepository tagFilterRepository;

    @Autowired
    private WorkbookRepository workbookRepository;

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 성공")
    @Test
    void findAllByWorkbookName() {
        // given
        initWorkbooks();

        // when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName("java");

        // then
        assertThat(tags).hasSize(6);
        assertThat(tags)
                .extracting(Tag::getTagNameValue)
                .contains("java", "자바", "자바짱", "jdk", "js", "javascript");
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 문제집의 카드가 0개일 때는 가져오지 않는다.")
    @Test
    void findAllByWorkbookNameWithCardZero() {
        // given
        initWorkbooks();

        // when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName("카드가 없는");

        // then
        assertThat(tags).hasSize(0);
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 비공개 문제집은 가져오지 않는다.")
    @Test
    void findAllByWorkbookNamePrivateWorkbook() {
        // given
        initWorkbooks();

        // when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName("비공개");

        // then
        assertThat(tags).hasSize(0);
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 성공, 일치하는 문제집 없음")
    @Test
    void findAllByWorkbookNameEmpty() {
        // given
        initWorkbooks();

        // when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName("babo");
        // then
        assertThat(tags).isEmpty();
    }

    @DisplayName("문제집명을 포함하는 문제집의 태그를 모두 가져온다. - 성공, 문제집 명이 null일 때 빈 리스트를 반환한다.")
    @Test
    void findAllByWorkbookNameNull() {
        // given
        initWorkbooks();

        // when
        List<Tag> tags = tagFilterRepository.findAllByContainsWorkbookName(null);
        // then
        assertThat(tags).isEmpty();
    }

    private void initWorkbooks() {

        Tag java = Tag.of("java");
        Tag 자바 = Tag.of("자바");
        Tag 자바짱 = Tag.of("자바짱");
        Tag jdk = Tag.of("jdk");
        Tag js = Tag.of("js");
        Tag javascript = Tag.of("javascript");
        Tag spring = Tag.of("Spring");

        Workbook workbookWithCardZero = Workbook.builder()
                .name("카드가 없는 문제집")
                .tags(Tags.of(List.of(java)))
                .build();

        List<Workbook> workbooks = List.of(
                makeWorkbookWithTwoTags("Java", 자바, java, true),
                makeWorkbookWithTwoTags("조앤의 Java 문제집", 자바, 자바짱, true),
                makeWorkbookWithTwoTags("오즈의 Java 문제집", java, jdk, true),
                makeWorkbookWithTwoTags("Javascript", javascript, js, true),
                makeWorkbookWithTwoTags("Spring", spring, java, true),
                makeWorkbookWithTwoTags("비공개 문제집", spring, java, false),
                workbookWithCardZero
        );
        workbookRepository.saveAll(workbooks);
    }

    private Workbook makeWorkbookWithTwoTags(String workbookName, Tag tag1, Tag tag2, boolean opened) {
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .opened(opened)
                .tags(Tags.of(List.of(tag1, tag2)))
                .build();
        workbook.addCard(Card.builder()
                .question("질문")
                .answer("답변")
                .build());
        return workbook;
    }
}
