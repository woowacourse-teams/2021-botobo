package botobo.core.domain.workbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkbookRepository extends JpaRepository<Workbook, Long> {

    @Query("select w from Workbook w where w.user.id = :userId order by w.createdAt desc")
    List<Workbook> findAllByUserId(@Param("userId") Long userId);

    @Query("select w from Workbook w left join fetch w.cards.cards c where w.id = :id order by c.createdAt desc")
    Optional<Workbook> findByIdAndOrderCardByNew(@Param("id") Long id);

    boolean existsByIdAndOpenedTrue(Long id);

    boolean existsById(Long id);

    @Query(value = "select w.*, count(c.id) as cnt from workbook w right join card c on w.id = c.workbook_id " +
            "where w.opened = true " +
            "and w.deleted = false " +
            "group by w.id having cnt > 0" +
            "order by RAND() " +
            "limit 100", nativeQuery = true)
    List<Workbook> findRandomPublicWorkbooks();
}

