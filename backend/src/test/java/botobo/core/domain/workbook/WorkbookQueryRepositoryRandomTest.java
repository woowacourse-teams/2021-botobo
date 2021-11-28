package botobo.core.domain.workbook;

import botobo.core.config.QuerydslConfig;
import botobo.core.domain.RepositoryTest;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.utils.UserFactory;
import botobo.core.utils.WorkbookFactory;
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
@Import({WorkbookQueryRepository.class, QuerydslConfig.class})
public class WorkbookQueryRepositoryRandomTest extends RepositoryTest {

    @Autowired
    private WorkbookQueryRepository workbookQueryRepository;

    @Autowired
    private WorkbookRepository workbookRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = UserFactory.user("1", "bear", "github.io", SocialType.GITHUB);
        userRepository.save(user);
    }

    @Test
    @DisplayName("공개 문제집을 랜덤하게 100개 조회한다. - 성공")
    void findRandomPublicWorkbooks() {
        // given
        saveWorkbooksWithOpenedSize(101, 0);

        // when
        List<Workbook> workbooks = workbookQueryRepository.findRandomPublicWorkbooks();

        // then
        assertThat(workbooks).hasSize(100);
    }

    @Test
    @DisplayName("공개 문제집을 랜덤하게 100개 조회한다. - 성공, 비공개 문제집은 조회하지 않는다.")
    void findRandomPublicWorkbooksIncludePrivate() {
        // given
        saveWorkbooksWithOpenedSize(90, 10);

        // when
        List<Workbook> workbooks = workbookQueryRepository.findRandomPublicWorkbooks();

        // then
        assertThat(workbooks).hasSize(90);
    }

    @Test
    @DisplayName("공개 문제집을 랜덤하게 100개 조회한다. - 성공")
    void findRandomPublicWorkbooksIncludeNonZero() {
        // given
        saveWorkbooksWithCard(100, 0);

        // when
        List<Workbook> workbooks = workbookQueryRepository.findRandomPublicWorkbooks();

        // then
        assertThat(workbooks).hasSize(100);
        for (Workbook workbook : workbooks) {
            assertThat(workbook.cardCount()).isPositive();
        }
    }

    @Test
    @DisplayName("공개 문제집을 랜덤하게 100개 조회한다. - 성공, 카드의 개수가 0개인 문제집은 조회하지 않는다.")
    void findRandomPublicWorkbooksIncludeNonZero2() {
        // given
        saveWorkbooksWithCard(90, 10);

        // when
        List<Workbook> workbooks = workbookQueryRepository.findRandomPublicWorkbooks();

        // then
        assertThat(workbooks).hasSize(90);
        for (Workbook workbook : workbooks) {
            assertThat(workbook.cardCount()).isPositive();
        }
    }

    private void saveWorkbooksWithOpenedSize(int publicSize, int privateSize) {
        for (int i = 0; i < publicSize; i++) {
            workbookRepository.save(
                    WorkbookFactory.workbook("Java 문제집" + i, user, 1, true, Tags.empty())
            );
        }
        for (int i = 0; i < privateSize; i++) {
            workbookRepository.save(
                    WorkbookFactory.workbook("Java 문제집" + i, user, 1, false, Tags.empty())
            );
        }
    }

    private void saveWorkbooksWithCard(int includeCardWorkbookSize, int excludeCardWorkbookSize) {
        for (int i = 0; i < includeCardWorkbookSize; i++) {
            workbookRepository.save(
                    WorkbookFactory.workbook("Java 문제집" + i, user, 1, true, Tags.empty())
            );
        }
        for (int i = 0; i < excludeCardWorkbookSize; i++) {
            workbookRepository.save(
                    WorkbookFactory.workbook("Java 문제집" + i, user, 0, true, Tags.empty())
            );
        }
    }
}
