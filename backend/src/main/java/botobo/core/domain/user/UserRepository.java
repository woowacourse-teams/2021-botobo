package botobo.core.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    boolean existsByIdAndRole(Long id, Role role);

    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}
