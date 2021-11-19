package botobo.core.domain.workbook;

import botobo.core.config.QuerydslConfig;
import botobo.core.domain.SearchRepositoryTest;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagName;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.workbooktag.WorkbookTag;
import botobo.core.ui.search.WorkbookSearchParameter;
import botobo.core.utils.WorkbookSearchParameterUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Import({WorkbookSearchRepository.class, QuerydslConfig.class})
class WorkbookSearchRepositoryTest extends SearchRepositoryTest {

    @Autowired
    private WorkbookSearchRepository workbookSearchRepository;

    @Test
    @DisplayName("검색어를 입력하면 문제집 이름 또는 태그에서 검색어가 포함된 것들을 가져온다. - 성공, 문제집 명에 포함")
    void searchAllFromKeyword() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("Java")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(6);
        assertThat(workbookList).extracting(Workbook::getName)
                .allMatch(name -> name.contains("Java"));
    }

    @Test
    @DisplayName("검색어를 입력하면 문제집 이름에서 검색어가 포함된 것들을 가져온다. - 성공, 태그와 일치")
    void searchAllFromKeywordWhenEqualsTagName() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("Joanne")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();


        // then
        assertThat(workbookList).hasSize(3);
        for (Workbook workbook : workbookList) {
            List<WorkbookTag> workbookTags = workbook.getWorkbookTags();
            assertThat(workbookTags).
                    extracting(WorkbookTag::getTag)
                    .extracting(Tag::getTagName)
                    .extracting(TagName::getValue)
                    .contains(parameter.getSearchKeyword().getValue());
        }

    }


    @Test
    @DisplayName("검색어와 태그 id를 입력하면 검색어 및 태그 id가 포함된 것들을 가져온다.")
    void searchAllFromKeywordAndTags() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("문제집")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(
                parameter,
                List.of(javaTag.getId()),
                null,
                parameter.toPageRequest()
        );
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList).extracting(Workbook::getName)
                .allMatch(name -> name.contains("문제집"));
        assertThat(workbookList).extracting(Workbook::tags)
                .allMatch(tags -> tags.countSameTagName(Tags.of(List.of(javaTag))) == 1);
    }

    @Test
    @DisplayName("검색어와 유저 id를 입력하면 검색어 및 유저 id가 포함된 것들을 가져온다.")
    void searchAllFromKeywordAndUsers() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("문제집")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(
                parameter,
                null,
                List.of(bear.getId()),
                parameter.toPageRequest()
        );
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList).extracting(Workbook::getName)
                .allMatch(name -> name.contains("문제집"));
        assertThat(workbookList).extracting(Workbook::author)
                .allMatch(name -> name.equals("bear"));
    }

    @Test
    @DisplayName("검색어와 태그 id, 유저 id를 입력하면 검색어 및 태그 id, 유저 id가 포함된 것들을 가져온다.")
    void searchAllFromKeywordAndTagsAndUsers() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("문제집")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(
                parameter,
                List.of(javaTag.getId()),
                Arrays.asList(bear.getId(), oz.getId()),
                parameter.toPageRequest()
        );
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList).extracting(Workbook::getName)
                .allMatch(name -> name.contains("문제집"));
        assertThat(workbookList).extracting(Workbook::tags)
                .allMatch(tags -> tags.countSameTagName(Tags.of(List.of(javaTag))) == 1);
        assertThat(workbookList).extracting(Workbook::author)
                .allMatch(name -> name.equals("bear"));
    }

    @Test
    @DisplayName("검색어를 입력하고 최신순으로 정렬한다.")
    void searchAllFromKeywordAndDateDesc() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("Javascript")
                .searchCriteria("date")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList).extracting(Workbook::getName)
                .containsExactly("Javascript 문제집2",
                        "Javascript 문제집1", "Javascript 문제집0");
    }

    @Test
    @DisplayName("검색어를 입력하고 이름순으로 정렬한다.")
    void searchAllFromKeywordAndNameAsc() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("Javascript")
                .searchCriteria("name")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList).extracting(Workbook::getName)
                .containsExactly("Javascript 문제집0",
                        "Javascript 문제집1", "Javascript 문제집2");
    }

    @Test
    @DisplayName("검색어를 입력하고 카드순으로 정렬한다.")
    void searchAllFromKeywordAndCardDesc() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("Javascript")
                .searchCriteria("count")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList).extracting(Workbook::getName)
                .containsExactly("Javascript 문제집2",
                        "Javascript 문제집1", "Javascript 문제집0");
    }

    @Test
    @DisplayName("검색어를 입력하고 좋아요순으로 정렬한다. 좋아요가 같다면 id순으로 정렬한다.")
    void searchAllFromKeywordAndHeartDesc() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("문제")
                .searchCriteria("heart")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(7);
        assertThat(workbookList).extracting(Workbook::getName)
                .containsExactly("좋아요가 많아 문제다",
                        "Java 문제집0",
                        "Java 문제집1",
                        "Java 문제집2",
                        "Javascript 문제집0",
                        "Javascript 문제집1",
                        "Javascript 문제집2");
    }

    @Test
    @DisplayName("검색어와 일치해도 카드가 없으면 조회되지 않는다.")
    void searchAllFromKeywordWhenNotExistCard() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("카드가 없다")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).isEmpty();
    }

    @Test
    @DisplayName("검색어와 일치해도 공개 문제집이 아니면 조회되지 않는다.")
    void searchAllFromKeywordWhenPrivate() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameterUtils.builder()
                .searchKeyword("비공개")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).isEmpty();
    }


    @DisplayName("유저아이디로 DownloadWorkbook을 조회해온다. - 성공")
    @Test
    void findAllDownloadWorkbooksByUserId() {
        // given
        DownloadWorkbooks downloadWorkbooks = workbookSearchRepository.findAllDownloadWorkbooksByUserId(bear.getId());

        // when - then
        // 카드가 없는 문제집 1개 제외
        assertThat(downloadWorkbooks.size()).isEqualTo(5);
    }

    private void initWorkbook(int publicWorkbookSize) {
        initUser();

        javaTag = Tag.of("Java");
        joanneTag = Tag.of("Joanne");
        javascriptTag = Tag.of("Javascript");

        for (int i = 0; i < publicWorkbookSize; i++) {
            Cards cards1 = new Cards();
            Cards cards2 = new Cards();
            for (int j = publicWorkbookSize; j > i; j--) {
                cards1.addCard(Card.builder()
                        .question("질문1")
                        .answer("답변1")
                        .build());
                cards1.addCard(Card.builder()
                        .question("질문2")
                        .answer("답변2")
                        .build());
                cards1.addCard(Card.builder()
                        .question("질문3")
                        .answer("답변3")
                        .build());
                cards2.addCard(Card.builder()
                        .question("질문")
                        .answer("답변")
                        .build());
            }
            Workbook workbook1 = Workbook.builder()
                    .name("Java 문제집" + i)
                    .opened(true)
                    .user(bear)
                    .tags(Tags.of(List.of(javaTag, joanneTag)))
                    .build();
            workbook1.addCards(cards1);
            workbookRepository.save(workbook1);

            Workbook workbook2 = Workbook.builder()
                    .name("Javascript 문제집" + i)
                    .opened(true)
                    .user(oz)
                    .tags(Tags.of(List.of(javascriptTag)))
                    .build();
            workbook2.addCards(cards2);
            workbookRepository.save(workbook2);
        }

        Workbook privateWorkbook = Workbook.builder()
                .name("비공개 문제집")
                .opened(false)
                .user(bear)
                .build();
        privateWorkbook.addCard(Card.builder()
                .question("질문")
                .answer("답변")
                .build());
        workbookRepository.save(privateWorkbook);


        workbookRepository.save(Workbook.builder()
                .name("카드가 없다")
                .opened(true)
                .user(bear)
                .build());

        Workbook heartWorkbook = Workbook.builder()
                .name("좋아요가 많아 문제다.")
                .opened(true)
                .user(bear)
                .build();
        heartWorkbook.addCard(Card.builder()
                .question("질문")
                .answer("답변")
                .build());
        heartWorkbook.toggleHeart(Heart.builder()
                .userId(1L)
                .workbook(heartWorkbook)
                .build());
        workbookRepository.save(heartWorkbook);
    }

    private void initUser() {
        bear = User.builder()
                .socialId("1")
                .userName("bear")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        oz = User.builder()
                .socialId("2")
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        userRepository.save(bear);
        userRepository.save(oz);
    }
}
