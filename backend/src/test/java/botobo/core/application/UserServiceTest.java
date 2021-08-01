package botobo.core.application;

import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.user.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("유저 서비스 테스트")
@MockitoSettings
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저 id에 해당하는 유저를 조회한다. - 성공")
    void findById() {
        // given
        User user = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("user")
                .profileUrl("profile.io")
                .build();
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        // when
        UserResponse userResponse = userService.findById(user.getId());

        // then
        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getUserName()).isEqualTo(user.getUserName());
        assertThat(userResponse.getProfileUrl()).isEqualTo(user.getProfileUrl());

        then(userRepository)
                .should(times(1))
                .findById(anyLong());
    }
}
