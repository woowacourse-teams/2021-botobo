package botobo.core.quiz.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c join fetch c.cards.cards where c.id = :id")
    Optional<Category> findCategoryAndCardsByIdJoinFetch(@Param("id") Long id);
}
