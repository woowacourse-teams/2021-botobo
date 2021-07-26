package botobo.core.quiz.domain.workbook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
    boolean existsByIdAndOpenedTrue(Long id);

    boolean existsById(Long id);

    List<Workbook> findAllByUserId(Long userId);
}
