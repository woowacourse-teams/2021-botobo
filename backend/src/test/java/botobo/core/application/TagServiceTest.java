package botobo.core.application;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.tag.TagResponse;
import botobo.core.dto.tag.WorkbookName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class TagServiceTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private WorkbookRepository workbookRepository;

    @Autowired
    private TagService tagService;

    @Test
    @DisplayName("태그 변환 - 성공, DB에 이미 존재하는 태그는 기존 태그를 가져오고 존재하지 않는 태그는 새로 생성된다.")
    void convertTags() {
        // given
        Tag java = tagRepository.save(Tag.of("java"));
        Tag spring = tagRepository.save(Tag.of("spring"));
        Tag react = tagRepository.save(Tag.of("react"));
        tagRepository.flush();

        List<TagRequest> tagRequests = Arrays.asList(
                TagRequest.builder().id(0L).name("javascript").build(),
                TagRequest.builder().id(1L).name("java").build(),
                TagRequest.builder().id(2L).name("spring").build(),
                TagRequest.builder().id(3L).name("react").build(),
                TagRequest.builder().id(0L).name("python").build()
        );

        // when
        Tags tags = tagService.convertTags(tagRequests);

        // then
        assertThat(tags.toList()).extracting("tagName")
                .extracting("value")
                .containsExactly("javascript", "java", "spring", "react", "python");
        assertThat(tags.toList())
                .extracting("id")
                .containsExactly(null, java.getId(), spring.getId(), react.getId(), null);
    }

    @Test
    @DisplayName("태그 중복 제거 - 성공, 입력에 같은 이름의 태그가 존재하면 중복을 제거하며 태그를 생성한다.")
    void convertTagsWithDistinction() {
        // given
        Tag java = tagRepository.save(Tag.of("java"));
        Tag spring = tagRepository.save(Tag.of("spring"));
        Tag react = tagRepository.save(Tag.of("react"));
        tagRepository.flush();

        List<TagRequest> tagRequests = Arrays.asList(
                TagRequest.builder().id(1L).name("java").build(),
                TagRequest.builder().id(2L).name("spring").build(),
                TagRequest.builder().id(3L).name("react").build(),
                TagRequest.builder().id(0L).name("java").build()
        );

        // when
        Tags tags = tagService.convertTags(tagRequests);

        // then
        assertThat(tags.toList()).extracting("tagName")
                .extracting("value")
                .containsExactly("java", "spring", "react");
        assertThat(tags.toList())
                .extracting("id")
                .containsExactly(java.getId(), spring.getId(), react.getId());
    }

    @DisplayName("문제집명이 포함된 문제집의 모든 태그를 가져온다. - 성공, 검색어는 소문자로 치환")
    @Test
    void findAllTagsByWorkbookName() {
        // given
        Tag java = Tag.of("java");
        Tag spring = Tag.of("spring");
        Tag react = Tag.of("react");

        List<Workbook> workbooks = List.of(
                makeWorkbookWithTwoTags("Java", java),
                makeWorkbookWithTwoTags("조앤의 Java 문제집", java),
                makeWorkbookWithTwoTags("오즈의 Java과 Spring 문제집", spring),
                makeWorkbookWithTwoTags("React", react),
                makeWorkbookWithTwoTags("Spring", spring)
        );
        workbookRepository.saveAll(workbooks);


        WorkbookName workbookName = new WorkbookName("JAVA");

        // when - then
        List<TagResponse> tagResponses = tagService.findAllTagsByWorkbookName(workbookName);
        assertThat(tagResponses).extracting("name")
                .containsExactly("java", "spring");
        assertThat(tagResponses).hasSize(2);
    }

    @DisplayName("문제집명이 포함된 문제집의 모든 태그를 가져온다. - 성공, 한글 검색")
    @Test
    void findAllTagsByWorkbookNameKor() {
        // given
        Tag java = Tag.of("java");
        Tag spring = Tag.of("spring");
        Tag react = Tag.of("react");

        List<Workbook> workbooks = List.of(
                makeWorkbookWithTwoTags("Java", java),
                makeWorkbookWithTwoTags("조앤의 Java 문제집", java),
                makeWorkbookWithTwoTags("오즈의 Java과 Spring 문제집", spring),
                makeWorkbookWithTwoTags("React", react),
                makeWorkbookWithTwoTags("Spring", spring)
        );
        workbookRepository.saveAll(workbooks);


        WorkbookName workbookName = new WorkbookName("문제집");

        // when - then
        List<TagResponse> tagResponses = tagService.findAllTagsByWorkbookName(workbookName);

        assertThat(tagResponses).extracting("name")
                .containsExactly("java", "spring");
        assertThat(tagResponses).hasSize(2);
    }

    @DisplayName("문제집명이 포함된 문제집의 모든 태그를 가져온다. - 성공, 빈 문자열일 경우 빈 리스트를 응답한다.")
    @Test
    void findAllTagsByWorkbookNameEmpty() {
        // given
        Tag java = Tag.of("java");
        Tag spring = Tag.of("spring");
        Tag react = Tag.of("react");

        List<Workbook> workbooks = List.of(
                makeWorkbookWithTwoTags("Java", java),
                makeWorkbookWithTwoTags("조앤의 Java 문제집", java),
                makeWorkbookWithTwoTags("오즈의 Java과 Spring 문제집", spring),
                makeWorkbookWithTwoTags("React", react),
                makeWorkbookWithTwoTags("Spring", spring)
        );
        workbookRepository.saveAll(workbooks);


        WorkbookName workbookName = new WorkbookName("");

        // when - then
        List<TagResponse> tagResponses = tagService.findAllTagsByWorkbookName(workbookName);

        assertThat(tagResponses).isEmpty();
    }

    private Workbook makeWorkbookWithTwoTags(String workbookName, Tag tag) {
        return Workbook.builder()
                .name(workbookName)
                .tags(Tags.of(List.of(tag)))
                .build();
    }
}
