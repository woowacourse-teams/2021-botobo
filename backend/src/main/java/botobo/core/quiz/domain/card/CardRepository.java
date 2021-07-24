package botobo.core.quiz.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByIdIn(List<Long> cardIds);

    @Query("select c from Card c where c.workbook.id in (:workbookIds)")
    List<Card> findCardsByWorkbookId(@Param("workbookIds") List<Long> workbookIds);
}
