package botobo.core.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(TagName tagName);

    @Query(value = "SELECT * FROM tag t WHERE t.NAME LIKE %:keyword% LIMIT 10", nativeQuery = true)
    List<Tag> findByKeyword(@Param("keyword") String keyword);
}
