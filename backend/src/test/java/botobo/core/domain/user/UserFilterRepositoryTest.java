package botobo.core.domain.user;

import botobo.core.config.QuerydslConfig;
import botobo.core.domain.card.Card;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import org.junit.jupiter.api.BeforeEach;
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
@Import({UserFilterRepository.class, QuerydslConfig.class})
class UserFilterRepositoryTest {

    @Autowired
    private UserFilterRepository userFilterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkbookRepository workbookRepository;

    private User user1, user2, user3;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .socialId("1")
                .userName("user1")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        user2 = User.builder()
                .socialId("2")
                .userName("user2")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        user3 = User.builder()
                .socialId("3")
                .userName("user3")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        initWorkbooks();
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 영어는 대소문자를 구분하지 않는다.")
    @Test
    void findAllByContainsWorkbookNameWhenEng() {
        // when
        List<Workbook> all = workbookRepository.findAll();
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("java");

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 일치하는 문제집 없음.")
    @Test
    void findAllByContainsWorkbookNameWhenEmptyResult() {
        // when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("바보");

        // then
        assertThat(users).isEmpty();
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공")
    @Test
    void findAllByContainsWorkbookName() {
        // when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("문제집");

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 카드가 없는 경우는 조회하지 않는다.")
    @Test
    void findAllByContainsWorkbookNameWhenCardsEmpty() {
        // when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("문제집");

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 비공개 문제집은 조회하지 않는다.")
    @Test
    void findAllByContainsWorkbookNameWhenOpened() {
        // when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName("문제집");

        // then
        assertThat(users).hasSize(2);
    }


    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 문제집 명이 null일 때 빈 리스트를 반환한다.")
    @Test
    void findAllByWorkbookNameNull() {
        // given - when
        List<User> users = userFilterRepository.findAllByContainsWorkbookName(null);

        // then
        assertThat(users).isEmpty();
    }


    private void initWorkbooks() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<Workbook> workbooks = List.of(
                makeWorkbookWithUser("자바문제집", user1),
                makeWorkbookWithUser("자바스크립트 문제집", user2),
                makeWorkbookWithUser("basic of java", user3),
                makeWorkbookWithUser("면접 질문 모음집", user1),
                makeWorkbookWithUser("JAVAscript cheatsheet", user2),
                makeWorkbookWithUser("리액트 기초 다루기", user3)
        );

        Workbook 카드없는문제집 = Workbook.builder()
                .name("카드 없는 문제집")
                .user(user3)
                .opened(true)
                .build();

        Card card = Card.builder()
                .answer("질문")
                .question("")
                .build();
        Workbook 비공개문제집 = Workbook.builder()
                .name("비공개 문제집")
                .user(user3)
                .opened(false)
                .build();
        비공개문제집.addCard(card);

        workbookRepository.saveAll(workbooks);
        workbookRepository.save(카드없는문제집);
        workbookRepository.save(비공개문제집);
    }

    private Workbook makeWorkbookWithUser(String workbookName, User user) {
        Card card = Card.builder()
                .answer("질문")
                .question("")
                .build();
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .user(user)
                .opened(true)
                .build();
        workbook.addCard(card);
        return workbook;
    }
}
