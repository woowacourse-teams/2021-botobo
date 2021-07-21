package botobo.core.quiz.domain.workbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
    @Query("select w from Workbook w join fetch w.cards.cards where w.id = :id")
    Optional<Workbook> findCategoryAndCardsByIdJoinFetch(@Param("id") Long id);
}
