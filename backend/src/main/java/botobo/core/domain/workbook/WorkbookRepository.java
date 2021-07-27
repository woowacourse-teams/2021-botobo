package botobo.core.domain.workbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
    boolean existsByIdAndOpenedTrue(Long id);

    boolean existsById(Long id);

    @Query("select w from Workbook w where w.user.id = :userId order by w.createdAt desc")
    List<Workbook> findAllByUserId(@Param("userId") Long userId);
}
