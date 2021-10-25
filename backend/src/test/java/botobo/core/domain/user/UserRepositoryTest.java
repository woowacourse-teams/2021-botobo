package botobo.core.domain.user;

import botobo.core.domain.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .socialId("1")
                .userName("user1")
                .profileUrl("profile.io")
                .role(Role.USER)
                .socialType(SocialType.GITHUB)
                .build();
    }

    @Test
    @DisplayName("User 저장 - 성공")
    void save() {
        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser).isSameAs(user);
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
        assertThat(savedUser.getBio()).isEqualTo("");
    }

    @Test
    @DisplayName("User id로 조회 - 성공")
    void findById() {
        // given
        User savedUser = userRepository.save(user);

        // when, then
        Optional<User> findUser = userRepository.findById(savedUser.getId());
        assertThat(findUser).containsSame(savedUser);
    }

    @Test
    @DisplayName("User Social Id와 Social Type 으로 조회 - 성공")
    void findByGithubId() {
        // given
        userRepository.save(user);

        // when, then
        Optional<User> findUser = userRepository.findBySocialIdAndSocialType(user.getSocialId(), SocialType.GITHUB);
        assertThat(findUser).containsSame(user);
    }

    @Test
    @DisplayName("UserName으로 조회 - 성공")
    void findByUserName() {
        // given
        userRepository.save(user);

        // when, then
        Optional<User> findUser = userRepository.findByUserName(user.getUserName());
        assertThat(findUser).containsSame(user);
    }
}
