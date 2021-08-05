package botobo.core.domain.workbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
    boolean existsByIdAndOpenedTrue(Long id);

    boolean existsById(Long id);

    @Query("select w from Workbook w where w.user.id = :userId order by w.createdAt desc")
    List<Workbook> findAllByUserId(@Param("userId") Long userId);

    @Query("select w from Workbook w left join fetch w.cards.cards c where w.id = :id order by c.createdAt desc")
    Optional<Workbook> findByIdAndOrderCardByNew(@Param("id") Long id);

    @Query("select w from Workbook w where w.name like %:name% and w.opened = true order by w.createdAt desc")
    List<Workbook> findAllOpenedByNameAndOrderByNew(@Param("name") String name);

    @Query("select w from Workbook w join fetch w.workbookTags wt where wt.tag.tagName.value = :tagName order by w.createdAt desc")
    List<Workbook> findAllOpenedByTagAndOrderByNew(@Param("tagName") String tagName);

    @Query("select w from Workbook w join fetch w.user u where u.userName = :authorName order by w.createdAt desc")
    List<Workbook> findAllOpenedByAuthorAndOrderByNew(@Param("authorName") String authorName);
}
