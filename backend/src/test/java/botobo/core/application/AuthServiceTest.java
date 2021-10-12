package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.exception.auth.TokenNotValidException;
import botobo.core.exception.user.AnonymousHasNotIdException;
import botobo.core.infrastructure.GithubOauthManager;
import botobo.core.infrastructure.JwtTokenProvider;
import botobo.core.infrastructure.OauthManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("로그인 서비스 테스트")
@MockitoSettings
class AuthServiceTest {

    @Mock
    private OauthManagerFactory oauthManagerFactory;

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
        UserInfoResponse githubUserInfoResponse = GithubUserInfoResponse.builder()
                .socialId("1")
                .userName("user")
                .profileUrl("user.io")
                .build();
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
                .profileUrl("user.io")
                .socialType(SocialType.GITHUB)
                .build();

        LoginRequest loginRequest = new LoginRequest("code");
        String accessToken = "토큰입니다";

        given(oauthManagerFactory.findOauthMangerBySocialType(any())).willReturn(githubOauthManager);
        given(githubOauthManager.getUserInfo(any())).willReturn(githubUserInfoResponse.toUser());
        given(userRepository.findBySocialIdAndSocialType(any(), any())).willReturn(Optional.of(user));
        given(jwtTokenProvider.createToken(user.getId())).willReturn(accessToken);

        // when
        TokenResponse tokenResponse = authService.createToken("github", loginRequest);

        // then
        assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);

        then(oauthManagerFactory)
                .should(times(1))
                .findOauthMangerBySocialType(any());
        then(githubOauthManager)
                .should(times(1))
                .getUserInfo(any());
        then(userRepository)
                .should(times(1))
                .findBySocialIdAndSocialType(any(), any());
        then(jwtTokenProvider)
                .should(times(1))
                .createToken(anyLong());
    }

    @Test
    @DisplayName("토큰을 만든다. - 성공, 새로운 유저인 경우")
    void createTokenWithNewUser() {
        // given
        UserInfoResponse githubUserInfoResponse = GithubUserInfoResponse.builder()
                .socialId("1")
                .userName("user")
                .profileUrl("user.io")
                .build();
        User user = User.builder()
                .id(1L)
                .socialId("1")
                .userName("user")
                .profileUrl("user.io")
                .build();

        LoginRequest loginRequest = new LoginRequest("code");
        String accessToken = "토큰입니다";

        given(oauthManagerFactory.findOauthMangerBySocialType(any())).willReturn(githubOauthManager);
        given(githubOauthManager.getUserInfo(any())).willReturn(githubUserInfoResponse.toUser());
        given(userRepository.findBySocialIdAndSocialType(any(), any())).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(user);
        given(jwtTokenProvider.createToken(user.getId())).willReturn(accessToken);

        // when
        TokenResponse tokenResponse = authService.createToken("github", loginRequest);

        // then
        assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);

        then(oauthManagerFactory)
                .should(times(1))
                .findOauthMangerBySocialType(any());
        then(githubOauthManager)
                .should(times(1))
                .getUserInfo(any());
        then(userRepository)
                .should(times(1))
                .findBySocialIdAndSocialType(any(), any());
        then(userRepository)
                .should(times(1))
                .save(any(User.class));
        then(jwtTokenProvider)
                .should(times(1))
                .createToken(anyLong());
    }

    @DisplayName("토큰으로 appUser를 찾는다. - 성공, 관리자가 아닌 경우")
    @Test
    void findAppUserByToken() {
        // given
        String credential = "credential";
        given(jwtTokenProvider.isValidToken(credential)).willReturn(Boolean.TRUE);
        given(jwtTokenProvider.getIdFromPayLoad(credential)).willReturn(1L);
        given(userRepository.existsByIdAndRole(1L, Role.ADMIN)).willReturn(Boolean.FALSE);

        // when
        AppUser appUser = authService.findAppUserByToken(credential);

        // then
        assertThat(appUser.getId()).isEqualTo(1L);
        assertThat(appUser.getRole()).isEqualTo(Role.USER);
    }

    @DisplayName("토큰으로 appUser를 찾는다. - 성공, 관리자의 경우")
    @Test
    void findAppUserByTokenWhenAdmin() {
        // given
        String credential = "credential";
        given(jwtTokenProvider.isValidToken(credential)).willReturn(Boolean.TRUE);
        given(jwtTokenProvider.getIdFromPayLoad(credential)).willReturn(1L);
        given(userRepository.existsByIdAndRole(1L, Role.ADMIN)).willReturn(Boolean.TRUE);

        // when
        AppUser appUser = authService.findAppUserByToken(credential);

        // then
        assertThat(appUser.getId()).isEqualTo(1L);
        assertThat(appUser.getRole()).isEqualTo(Role.ADMIN);
    }

    @DisplayName("토큰으로 appUser를 찾는다. - 토큰이 null인경우 익명의 유저 리턴")
    @Test
    void findAppUserByTokenWhenAnonymous() {
        // given
        String credential = null;

        // when
        AppUser appUser = authService.findAppUserByToken(credential);

        // then
        assertThatThrownBy(appUser::getId)
                .isInstanceOf(AnonymousHasNotIdException.class);
        assertThat(appUser.getRole()).isEqualTo(Role.ANONYMOUS);
    }

    @DisplayName("토큰으로 appUser를 찾는다. - 토큰이 유효하지 않은 경우 예외를 던진다.")
    @Test
    void findAppUserByTokenWhenInvalidToken() {
        // given
        String credential = "credential";
        given(jwtTokenProvider.isValidToken(credential)).willReturn(Boolean.FALSE);

        // when
        assertThatThrownBy(() -> authService.findAppUserByToken(credential))
                .isInstanceOf(TokenNotValidException.class);
    }
}
