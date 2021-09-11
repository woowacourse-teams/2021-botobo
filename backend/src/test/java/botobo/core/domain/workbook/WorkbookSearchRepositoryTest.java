package botobo.core.domain.workbook;

import botobo.core.config.QuerydslConfig;
import botobo.core.domain.card.Card;
import botobo.core.domain.card.Cards;
import botobo.core.domain.heart.Heart;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.ui.search.WorkbookSearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Import({WorkbookSearchRepository.class, QuerydslConfig.class})
class WorkbookSearchRepositoryTest {

    @Autowired
    private WorkbookRepository workbookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkbookSearchRepository workbookSearchRepository;

    private Tag javaTag, javascriptTag;
    private User bear, oz;

    @BeforeEach
    void setUp() {
        initWorkbook(3);
    }

    @Test
    @DisplayName("검색어를 입력하면 문제집 이름에서 검색어가 포함된 것들을 가져온다.")
    void searchAllFromKeyword() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameter.builder()
                .searchKeyword("Java")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(6);
        assertThat(workbookList.stream()
                .map(Workbook::getName)
                .allMatch(name -> name.contains("Java"))).isTrue();
    }

    @Test
    @DisplayName("검색어와 태그 id를 입력하면 검색어 및 태그 id가 포함된 것들을 가져온다.")
    void searchAllFromKeywordAndTags() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameter.builder()
                .searchKeyword("문제집")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter,
                Arrays.asList(javaTag.getId()), null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList.stream()
                .map(Workbook::getName)
                .allMatch(name -> name.contains("문제집"))).isTrue();
        assertThat(workbookList.stream()
                .map(Workbook::tags)
                .allMatch(tags -> tags.countSameTagName(Tags.of(Arrays.asList(javaTag))) == 1)).isTrue();
    }

    @Test
    @DisplayName("검색어와 유저 id를 입력하면 검색어 및 유저 id가 포함된 것들을 가져온다.")
    void searchAllFromKeywordAndUsers() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameter.builder()
                .searchKeyword("문제집")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null,
                Arrays.asList(bear.getId()), parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList.stream()
                .map(Workbook::getName)
                .allMatch(name -> name.contains("문제집"))).isTrue();
        assertThat(workbookList.stream()
                .map(Workbook::author)
                .allMatch(name -> name.equals("bear"))).isTrue();
    }

    @Test
    @DisplayName("검색어와 태그 id, 유저 id를 입력하면 검색어 및 태그 id, 유저 id가 포함된 것들을 가져온다.")
    void searchAllFromKeywordAndTagsAndUsers() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameter.builder()
                .searchKeyword("문제집")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter,
                Arrays.asList(bear.getId(), oz.getId()),
                Arrays.asList(javaTag.getId()), parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList.stream()
                .map(Workbook::getName)
                .allMatch(name -> name.contains("문제집"))).isTrue();
        assertThat(workbookList.stream()
                .map(Workbook::tags)
                .allMatch(tags -> tags.countSameTagName(Tags.of(Arrays.asList(javaTag))) == 1)).isTrue();
        assertThat(workbookList.stream()
                .map(Workbook::author)
                .allMatch(name -> name.equals("bear"))).isTrue();
    }

    @Test
    @DisplayName("검색어를 입력하고 최신순으로 정렬한다.")
    void searchAllFromKeywordAndDateDesc() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameter.builder()
                .searchKeyword("Javascript")
                .searchCriteria("date")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList.stream()
                .map(Workbook::getName)
                .collect(Collectors.toList())).containsExactly("Javascript 문제집2",
                "Javascript 문제집1", "Javascript 문제집0");
    }

    @Test
    @DisplayName("검색어를 입력하고 이름순으로 정렬한다.")
    void searchAllFromKeywordAndNameAsc() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameter.builder()
                .searchKeyword("Javascript")
                .searchCriteria("name")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList.stream()
                .map(Workbook::getName)
                .collect(Collectors.toList())).containsExactly("Javascript 문제집0",
                "Javascript 문제집1", "Javascript 문제집2");
    }

    @Test
    @DisplayName("검색어를 입력하고 카드순으로 정렬한다.")
    void searchAllFromKeywordAndCardDesc() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameter.builder()
                .searchKeyword("Javascript")
                .searchCriteria("count")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(3);
        assertThat(workbookList.stream()
                .map(Workbook::getName)
                .collect(Collectors.toList())).containsExactly("Javascript 문제집0",
                "Javascript 문제집1", "Javascript 문제집2");
    }

    @Test
    @DisplayName("검색어를 입력하고 좋아요순으로 정렬한다. 좋아요가 같다면 id순으로 정렬한다.")
    void searchAllFromKeywordAndHeartDesc() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameter.builder()
                .searchKeyword("문제")
                .searchCriteria("heart")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(7);
        assertThat(workbookList.stream()
                .map(Workbook::getName)
                .collect(Collectors.toList())).containsExactly("좋아요가 많아 문제다.",
                "Java 문제집0",
                "Javascript 문제집0",
                "Java 문제집1",
                "Javascript 문제집1",
                "Java 문제집2",
                "Javascript 문제집2");
    }

    @Test
    @DisplayName("검색어와 일치해도 카드가 없으면 조회되지 않는다.")
    void searchAllFromKeywordWhenNotExistCard() {
        // given
        WorkbookSearchParameter parameter = WorkbookSearchParameter.builder()
                .searchKeyword("카드가 없다")
                .build();

        // when
        Page<Workbook> workbooks = workbookSearchRepository.searchAll(parameter, null, null, parameter.toPageRequest());
        List<Workbook> workbookList = workbooks.toList();

        // then
        assertThat(workbookList).hasSize(0);
    }

    private void initWorkbook(int workbookSize) {
        initUser();
        javaTag = Tag.of("Java");
        javascriptTag = Tag.of("Javascript");
        for (int i = 0; i < workbookSize; i++) {
            Cards cards1 = new Cards();
            Cards cards2 = new Cards();
            for (int j = workbookSize; j > i; j--) {
                cards1.addCard(Card.builder()
                        .question("질문")
                        .answer("답변")
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
                    .tags(Tags.of(Arrays.asList(javaTag)))
                    .build();
            workbook1.addCards(cards1);
            workbookRepository.save(workbook1);

            Workbook workbook2 = Workbook.builder()
                    .name("Javascript 문제집" + i)
                    .opened(true)
                    .user(oz)
                    .tags(Tags.of(Arrays.asList(javascriptTag)))
                    .build();
            workbook2.addCards(cards2);
            workbookRepository.save(workbook2);
        }
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
