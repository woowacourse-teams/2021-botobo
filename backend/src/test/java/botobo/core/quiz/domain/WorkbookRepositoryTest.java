package botobo.core.quiz.domain;

import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
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
                .isDeleted(false)
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

//    @Test
//    @DisplayName("Workbook 저장 - 실패, 이름이 최대 길이를 초과")
//    void saveWithLongName() {
//        // given
//        Workbook workbook = Workbook.builder()
//                .name("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz")
//                .isDeleted(false)
//                .build();
//
//        // when, then
//        assertThatThrownBy(() -> workbookRepository.save(workbook))
//                .isInstanceOf(DataIntegrityViolationException.class);
//    }

    @Test
    @DisplayName("Workbook id로 조회 - 성공")
    void findById() {
        // given
        Workbook workbook = Workbook.builder()
                .name("java")
                .isDeleted(false)
                .build();
        Workbook savedWorkbook = workbookRepository.save(workbook);

        // when, then
        Optional<Workbook> findWorkbook = workbookRepository.findById(savedWorkbook.getId());
        assertThat(findWorkbook).containsSame(savedWorkbook);
    }
}