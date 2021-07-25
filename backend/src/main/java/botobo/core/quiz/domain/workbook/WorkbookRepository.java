package botobo.core.quiz.domain.workbook;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
    boolean existsById(Long id);
}
