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

    @Query(value = "select * from workbook w " +
            "where (select count(*) from card c where c.workbook_id = w.id group by workbook_id) > 0 " +
            "and opened = true " +
            "order by RAND() " +
            "limit 100", nativeQuery = true)
    List<Workbook> findRandomPublicWorkbooks();
}

