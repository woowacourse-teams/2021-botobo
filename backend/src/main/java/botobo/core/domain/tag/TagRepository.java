package botobo.core.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(TagName tagName);

    @Query("select t from Tag t where t.tagName.value like %:keyword%")
    List<Tag> findAllTagContaining(@Param("keyword") String keyword);
    @Query(value = "SELECT * FROM tag t WHERE t.NAME LIKE %:keyword% LIMIT 10", nativeQuery = true)
    List<Tag> findByKeyword(@Param("keyword") String keyword);

    @Query("select t from Tag t join fetch t.workbookTags wt " +
            "where t.id = wt.tag.id " +
            "and wt.workbook.name like %:workbookName%")
    List<Tag> findAllByWorkbookName(String workbookName);
}
