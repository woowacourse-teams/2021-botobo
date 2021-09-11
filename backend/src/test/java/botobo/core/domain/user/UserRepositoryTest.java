package botobo.core.domain.user;

import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
class UserRepositoryTest {

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

    }

    @Test
    @DisplayName("User 저장 - 성공")
    void save() {
        // when
        User savedUser = userRepository.save(user1);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser).isSameAs(user1);
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
        assertThat(savedUser.getBio()).isEqualTo("");
    }

    @Test
    @DisplayName("User id로 조회 - 성공")
    void findById() {
        // given
        User savedUser = userRepository.save(user1);

        // when, then
        Optional<User> findUser = userRepository.findById(savedUser.getId());
        assertThat(findUser).containsSame(savedUser);
    }

    @Test
    @DisplayName("User Social Id와 Social Type 으로 조회 - 성공")
    void findByGithubId() {
        // given
        userRepository.save(user1);

        // when, then
        Optional<User> findUser = userRepository.findBySocialIdAndSocialType(user1.getSocialId(), SocialType.GITHUB);
        assertThat(findUser).containsSame(user1);
    }

    @Test
    @DisplayName("UserName으로 조회 - 성공")
    void findByUserName() {
        // given
        userRepository.save(user1);

        // when, then
        Optional<User> findUser = userRepository.findByUserName(user1.getUserName());
        assertThat(findUser).containsSame(user1);
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공")
    @Test
    void findAllByContainsWorkbookName() {
        // given
        initWorkbooks();

        // when
        List<User> users = userRepository.findAllByContainsWorkbookName("문제집");

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 영어는 대소문자를 구분하지 않는다.")
    @Test
    void findAllByContainsWorkbookNameWhenEng() {
        // given
        initWorkbooks();

        // when
        List<User> users = userRepository.findAllByContainsWorkbookName("java");

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("문제집명을 포함한 문제집의 유저를 모두 가져온다. - 성공, 일치하는 문제집 없음.")
    @Test
    void findAllByContainsWorkbookNameWhenEmptyResult() {
        // given
        initWorkbooks();

        // when
        List<User> users = userRepository.findAllByContainsWorkbookName("바보");

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

        workbookRepository.saveAll(workbooks);
    }

    private Workbook makeWorkbookWithUser(String workbookName, User user) {
        return Workbook.builder()
                .name(workbookName)
                .user(user)
                .build();
    }
}
