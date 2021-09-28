package botobo.core.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    boolean existsByIdAndRole(Long id, Role role);

    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    @Query("select distinct u from User u join fetch u.workbooks w where lower(w.name) like %:workbookName% ")
    List<User> findAllByContainsWorkbookName(@Param("workbookName") String workbook);
}
