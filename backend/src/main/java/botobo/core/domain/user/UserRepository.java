package botobo.core.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    @Query(value = "SELECT * FROM user u WHERE u.user_name LIKE %:keyword% LIMIT 10", nativeQuery = true)
    List<User> findByKeyword(@Param("keyword") String keyword);

    boolean existsByIdAndRole(Long id, Role role);

    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}
