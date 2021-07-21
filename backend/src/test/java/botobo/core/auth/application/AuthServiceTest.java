package botobo.core.auth.application;

import botobo.core.auth.dto.GithubUserInfoResponse;
import botobo.core.auth.dto.TokenResponse;
import botobo.core.auth.infrastructure.GithubOauthManager;
import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.user.domain.User;
import botobo.core.user.domain.UserRepository;
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

@DisplayName("로그인 서비스 테스트")
@MockitoSettings
class AuthServiceTest {

    @Mock
    private GithubOauthManager githubOauthManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("토큰을 만든다. - 성공, 유저가 이미 존재하는 경우")
    void createToken() {
        // given
        GithubUserInfoResponse githubUserInfoResponse = GithubUserInfoResponse.builder()
                .githubId(1L)
                .userName("user")
                .profileUrl("user.io")
                .build();
        User user = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("user")
                .profileUrl("user.io")
                .build();

        String accessToken = "토큰입니다";

        given(githubOauthManager.getUserInfoFromGithub(any())).willReturn(githubUserInfoResponse);
        given(userRepository.findByGithubId(anyLong())).willReturn(Optional.of(user));
        given(jwtTokenProvider.createToken(user.getId())).willReturn(accessToken);

        // when
        TokenResponse tokenResponse = authService.createToken(any());

        // then
        assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);

        then(githubOauthManager)
                .should(times(1))
                .getUserInfoFromGithub(any());
        then(userRepository)
                .should(times(1))
                .findByGithubId(anyLong());
        then(jwtTokenProvider)
                .should(times(1))
                .createToken(anyLong());
    }

    @Test
    @DisplayName("토큰을 만든다. - 성공, 새로운 유저인 경우")
    void createTokenWithNewUser() {
        // given
        GithubUserInfoResponse githubUserInfoResponse = GithubUserInfoResponse.builder()
                .githubId(1L)
                .userName("user")
                .profileUrl("user.io")
                .build();
        User user = User.builder()
                .id(1L)
                .githubId(1L)
                .userName("user")
                .profileUrl("user.io")
                .build();

        String accessToken = "토큰입니다";

        given(githubOauthManager.getUserInfoFromGithub(any())).willReturn(githubUserInfoResponse);
        given(userRepository.findByGithubId(anyLong())).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(user);
        given(jwtTokenProvider.createToken(user.getId())).willReturn(accessToken);

        // when
        TokenResponse tokenResponse = authService.createToken(any());

        // then
        assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);

        then(githubOauthManager)
                .should(times(1))
                .getUserInfoFromGithub(any());
        then(userRepository)
                .should(times(1))
                .findByGithubId(anyLong());
        then(userRepository)
                .should(times(1))
                .save(any(User.class));
        then(jwtTokenProvider)
                .should(times(1))
                .createToken(anyLong());
    }
}