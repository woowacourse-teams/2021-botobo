package botobo.core.domain;

import botobo.core.domain.card.Card;
import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FilterRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkbookRepository workbookRepository;

    private User USER1, USER2, USER3, USER4, USER5, USER6;


    @BeforeEach
    void setUp() {
        USER1 = User.builder()
                .socialId("1")
                .userName("user1")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        USER2 = User.builder()
                .socialId("2")
                .userName("user2")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        USER3 = User.builder()
                .socialId("3")
                .userName("user3")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        USER4 = User.builder()
                .socialId("4")
                .userName("user4")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        USER5 = User.builder()
                .socialId("5")
                .userName("user5")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();

        USER6 = User.builder()
                .socialId("6")
                .userName("user6")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();
        initWorkbooks();
    }

    protected void initWorkbooks() {

        userRepository.save(USER1);
        userRepository.save(USER2);
        userRepository.save(USER3);
        userRepository.save(USER4);
        userRepository.save(USER5);
        userRepository.save(USER6);

        Tag java = Tag.of("java");
        Tag 자바 = Tag.of("자바");
        Tag 자바짱 = Tag.of("자바짱");
        Tag jdk = Tag.of("jdk");
        Tag js = Tag.of("js");
        Tag javascript = Tag.of("javascript");
        Tag spring = Tag.of("Spring");
        Tag notOpened = Tag.of("private");
        Tag empty = Tag.of("empty");

        Workbook workbookWithCardZero = Workbook.builder()
                .name("카드가 없는 문제집")
                .tags(Tags.of(List.of(empty)))
                .user(USER1)
                .build();

        List<Workbook> workbooks = List.of(
                makeWorkbookWithTwoTagsAndUser("Java", 자바, java, true, USER1),
                makeWorkbookWithTwoTagsAndUser("조앤의 Java 문제집", 자바, 자바짱, true, USER2),
                makeWorkbookWithTwoTagsAndUser("오즈의 Java 문제집", java, jdk, true, USER3),
                makeWorkbookWithTwoTagsAndUser("Javascript", javascript, js, true, USER4),
                makeWorkbookWithTwoTagsAndUser("Spring", spring, java, true, USER5),
                makeWorkbookWithTwoTagsAndUser("비공개 문제집", notOpened, java, false, USER6),
                workbookWithCardZero
        );
        workbookRepository.saveAll(workbooks);
    }

    protected Workbook makeWorkbookWithTwoTagsAndUser(String workbookName,
                                                      Tag tag1,
                                                      Tag tag2,
                                                      boolean opened,
                                                      User user) {
        Workbook workbook = Workbook.builder()
                .name(workbookName)
                .user(user)
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
