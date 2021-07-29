package botobo.core.domain.workbook;

import botobo.core.domain.card.Card;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.exception.NotAuthorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class WorkbookRepositoryTest {

    @Autowired
    private WorkbookRepository workbookRepository;

    @Autowired
    private UserRepository userRepository;

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
                .githubId(1L)
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
                .githubId(1L)
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        Workbook workbook = Workbook.builder()
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(user)
                .build();
        workbookRepository.save(workbook);

        // when
        workbook.updateIfUserIsOwner("오즈의 Java를 다 잡아", false, user.getId());
        testEntityManager.flush();

        // then
        assertThat(workbook.getName()).isEqualTo("오즈의 Java를 다 잡아");
        assertThat(workbook.isOpened()).isFalse();
        assertThat(workbook.getUpdatedAt()).isAfter(workbook.getCreatedAt());
    }

    @Test
    @DisplayName("유저가 자신의 문제집을 수정한다. - 실패, 다른 유저가 수정을 시도할 경우")
    void updateWorkbookWithOtherUser() {
        // given
        User user1 = User.builder()
                .githubId(1L)
                .userName("oz")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .githubId(2L)
                .userName("pk")
                .profileUrl("github.io")
                .role(Role.USER)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        Workbook workbook = Workbook.builder()
                .name("오즈의 Java")
                .opened(true)
                .deleted(false)
                .user(user1)
                .build();
        workbookRepository.save(workbook);

        // when, then
        assertThatThrownBy(() ->
                workbook.updateIfUserIsOwner("오즈의 Java를 다 잡아", false, 2L))
                .isInstanceOf(NotAuthorException.class);
    }

    @Test
    @DisplayName("유저가 자신의 문제집을 삭제한다. - 성공")
    void deleteWorkbook() {
        // given
        User user = User.builder()
                .githubId(1L)
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
        workbookRepository.delete(workbook);

        //then
        assertThat(workbookRepository.findAllByUserId(user.getId())).hasSize(0);
    }
}