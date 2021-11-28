package botobo.core.domain;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.domain.workbook.Workbook;
import botobo.core.domain.workbook.WorkbookRepository;
import botobo.core.utils.UserFactory;
import botobo.core.utils.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class QueryRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkbookRepository workbookRepository;

    protected User bear, oz;
    protected Tag javaTag, javascriptTag, joanneTag, notOpened, empty;

    @BeforeEach
    void setUp() {
        bear = UserFactory.user("1", "bear", "github.io", SocialType.GITHUB);
        oz = UserFactory.user("2", "oz", "github.io", SocialType.GOOGLE);

        userRepository.save(bear);
        userRepository.save(oz);

        initWorkbooks();
    }

    protected void initWorkbooks() {
        javaTag = Tag.of("java");
        joanneTag = Tag.of("joanne");
        javascriptTag = Tag.of("javascript");
        notOpened = Tag.of("private");
        empty = Tag.of("empty");

        Tags tags1 = Tags.of(Arrays.asList(javaTag, joanneTag));
        Tags tags2 = Tags.of(List.of(javascriptTag));

        List<Workbook> workbooks = List.of(
                WorkbookFactory.workbook("Java 문제집0", bear, 1, true, tags1),
                WorkbookFactory.workbook("Java 문제집1", bear, 2, true, tags1),
                WorkbookFactory.workbook("Java 문제집2", bear, 3, true, tags1),
                WorkbookFactory.workbook("Javascript 문제집0", oz, 1, true, tags2),
                WorkbookFactory.workbook("Javascript 문제집1", oz, 2, true, tags2),
                WorkbookFactory.workbook("Javascript 문제집2", oz, 3, true, tags2),
                WorkbookFactory.workbook("비공개 문제집", bear, 1, false, notOpened),
                WorkbookFactory.workbook("카드가 없다", bear, 0, true, empty),
                WorkbookFactory.workbook("좋아요가 많아 문제다", bear, 1, true, oz, Tags.empty())
        );
        workbookRepository.saveAll(workbooks);
    }
}
