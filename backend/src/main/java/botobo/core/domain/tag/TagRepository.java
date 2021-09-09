package botobo.core.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(TagName tagName);

    @Query("select t from Tag t where t.tagName.value like %:keyword%")
    List<Tag> findAllTagContaining(@Param("keyword") String keyword);

    @Query("select distinct t from Tag t join fetch t.workbookTags wt " +
            "where lower(wt.workbook.name) like %:workbookName%")
    List<Tag> findAllByContainsWorkbookName(@Param("workbookName") String workbookName);
}
