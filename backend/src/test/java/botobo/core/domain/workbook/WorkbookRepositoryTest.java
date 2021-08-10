package botobo.core.domain.workbook;

import botobo.core.domain.card.Card;
import botobo.core.domain.heart.Heart;
import botobo.core.domain.heart.HeartRepository;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagRepository;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbooktag.WorkbookTag;
import botobo.core.domain.workbooktag.WorkbookTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class WorkbookRepositoryTest {

    @Autowired
    private WorkbookRepository workbookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkbookTagRepository workbookTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private HeartRepository heartRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Workbook 저장 - 성공")
    void save() {
        // given
        Workbook workbook = Workbook.builder()
                .name("중간곰의 스프링 완전정복")
                .deleted(false)
                .build();

        // when
        Workbook savedWorkbook = workbookRepository.save(workbook);

        // then
        assertThat(workbook.getId()).isNotNull();
        assertThat(savedWorkbook).extracting("id").isNotNull();
        assertThat(savedWorkbook).isSameAs(workbook);
        assertThat(savedWorkbook.getCreatedAt()).isNotNull();
        assertThat(savedWorkbook.getUpdatedAt()).isNotNull();
        testEntityManager.flush();
    }

    @Test
    @DisplayName("Workbook id로 조회 - 성공")
    void findById() {
        // given
        Workbook workbook = Workbook.builder()
                .name("java")
                .deleted(false)
                .build();
        Workbook savedWorkbook = workbookRepository.save(workbook);

        // when, then
        Optional<Workbook> findWorkbook = workbookRepository.findById(savedWorkbook.getId());
        assertThat(findWorkbook).containsSame(savedWorkbook);
    }

    @Test
    @DisplayName("Public하고 Id가 존재하는 Workbook 조회 - 성공")
    void isPublic() {
        // given
        Workbook workbook = Workbook.builder()
                .name("java")
                .deleted(false)
                .opened(true)
                .build();
        Workbook savedWorkbook = workbookRepository.save(workbook);

        // when, then
        assertThat(workbookRepository.existsByIdAndOpenedTrue(savedWorkbook.getId())).isTrue();
    }

    @Test
    @DisplayName("워크북 조회할 때 포함된 카드가 최근에 생성된 순으로 정렬 - 성공")
    void findByIdAndOrderCardByNew() {
        // given
        Workbook workbook = Workbook.builder()
                .name("피케이의 자바 100문")
                .deleted(false)
                .opened(true)
                .build();
        Card firstCard = generateCard(workbook);
        Card secondCard = generateCard(workbook);
        Card thirdCard = generateCard(workbook);

        workbookRepository.save(workbook);

        testEntityManager.flush();
        testEntityManager.clear();

        // when
        Optional<Workbook> findWorkbook = workbookRepository.findByIdAndOrderCardByNew(workbook.getId());

        // then
        List<Card> cards = findWorkbook.get().getCards().getCards();
        assertThat(cards).hasSize(3)
                .containsExactly(thirdCard, secondCard, firstCard);
    }

    private Card generateCard(Workbook workbook) {
        return Card.builder()
                .question("질문")
                .answer("답변")
                .workbook(workbook)
                .build();
    }

    @Test
    @DisplayName("유저의 Workbook 최신순으로 조회 - 성공")
    void findAllByUserId() {
        // given
        User user = User.builder()
                .socialId("1")
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        Workbook workbook1 = Workbook.builder()
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(user)
                .build();

        workbookRepository.save(workbook1);

        flushAndClear();

        Workbook workbook2 = Workbook.builder()
                .name("오즈의 Spring")
                .opened(true)
                .deleted(false)
                .user(user)
                .build();

        workbookRepository.save(workbook2);

        // when
        List<Workbook> workbooks = workbookRepository.findAllByUserId(user.getId());

        //then
        assertThat(workbooks).hasSize(2)
                .containsExactly(workbook2, workbook1);
    }

    @Test
    @DisplayName("유저가 자신의 문제집을 수정한다. - 성공")
    void updateWorkbook() {
        // given
        User user = User.builder()
                .socialId("1")
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        Tags tags = Tags.of(Arrays.asList(
                Tag.of("잡아"), Tag.of("javi"))
        );

        Workbook workbook = Workbook.builder()
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(user)
                .tags(tags)
                .build();
        workbookRepository.save(workbook);

        Workbook updateWorkbook = Workbook.builder()
                .name("오즈의 Java를 다 잡아")
                .opened(false)
                .tags(Tags.of(
                        Arrays.asList(Tag.of("자바"), Tag.of("java"), Tag.of("오즈"))
                ))
                .build();

        // when
        workbook.update(updateWorkbook);
        testEntityManager.flush();

        // then
        assertThat(workbook.getName()).isEqualTo(updateWorkbook.getName());
        assertThat(workbook.isOpened()).isFalse();
        assertThat(workbook.getUpdatedAt()).isAfter(workbook.getCreatedAt());
        assertThat(workbook.getWorkbookTags())
                .extracting("tag")
                .extracting("tagName")
                .extracting("value")
                .containsExactly("자바", "java", "오즈");
    }

    @Test
    @DisplayName("유저가 자신의 문제집을 삭제한다. - 성공")
    void deleteWorkbook() {
        // given
        User user = User.builder()
                .socialId("1")
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        Workbook workbook = Workbook.builder()
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .build()
                .createBy(user);

        workbookRepository.save(workbook);

        // when
        workbook.delete();

        //then
        assertThat(workbookRepository.findAllByUserId(user.getId())).hasSize(0);
    }

    @DisplayName("Cascade 검사 - 성공, Tags와 함께 Workbook 저장시 WorkbookTag와 Tag도 함께 저장된다.")
    @Test
    void saveWorkbookWithTags() {
        // given
        Tags tags = Tags.of(Arrays.asList(
                Tag.of("java"), Tag.of("자바")
        ));

        Workbook workbook = Workbook.builder()
                .name("Java 문제집")
                .tags(tags)
                .build();

        // when
        Workbook savedWorkbook = workbookRepository.save(workbook);
        flushAndClear();

        // then
        List<Tag> workbookTags = savedWorkbook.tags().toList();
        List<WorkbookTag> dbWorkbookTags = workbookTagRepository.findAll();
        List<Tag> dbTags = tagRepository.findAll();

        assertThat(workbookTags).hasSize(2);
        assertThat(dbWorkbookTags).hasSize(2);
        assertThat(dbTags).hasSize(2);
    }

    @DisplayName("Cascade 검사 - 성공, Workbook 삭제시 WorkbookTag는 함께 삭제되고, Tag는 삭제되지 않는다. ")
    @Test
    void deleteWorkbookWithOrphanWorkbookTags() {
        // given
        Tags tags = Tags.of(Arrays.asList(
                Tag.of("java"), Tag.of("자바")
        ));

        Workbook workbook = Workbook.builder()
                .name("Java 문제집")
                .tags(tags)
                .build();

        Workbook savedWorkbook = workbookRepository.save(workbook);
        flushAndClear();

        // when
        workbookRepository.delete(workbook);
        flushAndClear();

        // then
        assertThat(workbookRepository.findAll()).isEmpty();
        assertThat(workbookTagRepository.findAll()).isEmpty();
        assertThat(tagRepository.findAll()).hasSize(2);
    }

    @DisplayName("Workbook이 Heart의 추가를 관리한다.")
    @Test
    void createHeartFromWorkbook() {
        // given
        User user = User.builder()
                .socialId("1")
                .userName("bear")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();

        Workbook workbook = workbookRepository.save(
                Workbook.builder()
                        .name("Java 문제집")
                        .user(user)
                        .build()
        );

        userRepository.save(user);
        workbookRepository.save(workbook);

        flushAndClear();

        Workbook savedWorkbook = workbookRepository.findById(workbook.getId()).get();
        assertThat(savedWorkbook.getHearts().getHearts()).hasSize(0);

        // when
        Heart heart = Heart.builder().workbook(workbook).userId(user.getId()).build();
        savedWorkbook.toggleHeart(heart);

        // then
        assertThat(savedWorkbook.getHearts().getHearts()).hasSize(1);
    }

    @DisplayName("orphanRemoval 검사 - 성공, Workbook이 Heart의 삭제를 관리한다.")
    @Test
    void deleteHeartFromWorkbook() {
        // given
        User user = User.builder()
                .socialId("1")
                .userName("bear")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();

        Workbook workbook = workbookRepository.save(
                Workbook.builder()
                        .name("Java 문제집")
                        .user(user)
                        .build()
        );

        userRepository.save(user);
        workbookRepository.save(workbook);

        Heart heart = Heart.builder().workbook(workbook).userId(user.getId()).build();
        heartRepository.save(heart);

        flushAndClear();

        Workbook savedWorkbook = workbookRepository.findById(workbook.getId()).get();
        assertThat(savedWorkbook.getHearts().getHearts()).hasSize(1);

        // when
        savedWorkbook.toggleHeart(heart);

        // then
        assertThat(savedWorkbook.getHearts().getHearts()).hasSize(0);
    }

    private void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}