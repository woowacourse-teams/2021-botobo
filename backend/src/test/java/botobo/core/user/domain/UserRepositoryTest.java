package botobo.core.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 저장 - 성공")
    void save() {
        // given
        User user = User.builder()
                .githubId(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser).isSameAs(user);
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("User id로 조회 - 성공")
    void findById() {
        // given
        User user = User.builder()
                .githubId(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build();
        User savedUser = userRepository.save(user);

        // when, then
        Optional<User> findUser = userRepository.findById(savedUser.getId());
        assertThat(findUser).containsSame(savedUser);
    }

    @Test
    @DisplayName("User Github Id로 조회 - 성공")
    void findByGithubId() {
        // given
        User user = User.builder()
                .githubId(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build();
        userRepository.save(user);

        // when, then
        Optional<User> findUser = userRepository.findByGithubId(user.getGithubId());
        assertThat(findUser).containsSame(user);
    }
}
