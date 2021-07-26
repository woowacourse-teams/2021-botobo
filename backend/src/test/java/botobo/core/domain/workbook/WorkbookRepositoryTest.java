package botobo.core.domain.workbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WorkbookRepositoryTest {

    @Autowired
    private WorkbookRepository workbookRepository;

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
}