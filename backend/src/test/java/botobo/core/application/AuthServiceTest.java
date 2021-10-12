package botobo.core.application;

import botobo.core.domain.token.RefreshToken;
import botobo.core.domain.token.RefreshTokenRepository;
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
import botobo.core.infrastructure.auth.GithubOauthManager;
import botobo.core.infrastructure.auth.JwtTokenProvider;
import botobo.core.infrastructure.auth.JwtTokenType;
import botobo.core.infrastructure.auth.OauthManagerFactory;
import botobo.core.utils.YamlLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

@DisplayName("로그인 서비스 테스트")
@MockitoSettings
class AuthServiceTest {

    private static final String RESOURCE_FILE_NAME = "application-test.yml";
    private static final String FULL_KEY = "security.jwt.refresh-token.expire-length";
    private static final Long TIME_TO_LIVE = Long.valueOf(
            (Integer) Objects.requireNonNull(YamlLoader.extractValue(RESOURCE_FILE_NAME, FULL_KEY))
    );

    @Mock
    private OauthManagerFactory oauthManagerFactory;

    @Mock
    private GithubOauthManager githubOauthManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("토큰을 만든다. - 성공, 유저가 이미 존재하는 경우")
    void createAccessToken() {
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
        given(jwtTokenProvider.createAccessToken(user.getId())).willReturn(accessToken);

        // when
        TokenResponse tokenResponse = authService.createAccessToken("github", loginRequest);

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
                .createAccessToken(anyLong());
    }

    @Test
    @DisplayName("토큰을 만든다. - 성공, 새로운 유저인 경우")
    void createAccessTokenWithNewUser() {
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
        given(jwtTokenProvider.createAccessToken(user.getId())).willReturn(accessToken);

        // when
        TokenResponse tokenResponse = authService.createAccessToken("github", loginRequest);

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
                .createAccessToken(anyLong());
    }

    @Test
    @DisplayName("리프레시 토큰을 생성한다 - 성공")
    void createRefreshToken() {
        // given
        Long id = 1L;
        String refreshTokenValue = "refreshTokenValue";
        RefreshToken refreshToken = new RefreshToken(id, refreshTokenValue, TIME_TO_LIVE);

        given(jwtTokenProvider.getJwtRefreshTokenTimeToLive()).willReturn(TIME_TO_LIVE);
        given(jwtTokenProvider.createRefreshToken(1L)).willReturn(refreshTokenValue);
        given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(refreshToken);

        // when
        authService.createRefreshToken(id);

        // then
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        then(refreshTokenRepository).should().save(captor.capture());
        RefreshToken actual = captor.getValue();
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(refreshToken);

        then(jwtTokenProvider)
                .should(times(1))
                .createRefreshToken(1L);
        then(jwtTokenProvider)
                .should(times(1))
                .getJwtRefreshTokenTimeToLive();
        then(refreshTokenRepository)
                .should(times(1))
                .save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("리프레시 토큰 검증 - 성공")
    void validateRefreshTokenTest() {
        // given
        String id = "1";
        String tokenValue = "middleBearPk";
        RefreshToken storedRefreshToken = new RefreshToken(id, tokenValue, TIME_TO_LIVE);

        given(jwtTokenProvider.getStringIdFromPayLoad(tokenValue, JwtTokenType.REFRESH_TOKEN)).willReturn(id);
        given(refreshTokenRepository.findById(id)).willReturn(Optional.of(storedRefreshToken));

        // when, then
        assertThatCode(() -> authService.validateRefreshToken(tokenValue))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("리프레시 토큰 검증 - 실패, 저장하지 않은 토큰")
    void validateRefreshTokenWithNonexistentId() {
        // given
        String id = "1";
        String tokenValue = "middleBearPk";

        given(jwtTokenProvider.getStringIdFromPayLoad(tokenValue, JwtTokenType.REFRESH_TOKEN)).willReturn(id);
        given(refreshTokenRepository.findById(id)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> authService.validateRefreshToken(tokenValue))
                .isExactlyInstanceOf(TokenNotValidException.class);
    }

    @Test
    @DisplayName("리프레시 토큰 검증 - 실패, 최신에 발급받은 토큰이 아님")
    void validateRefreshTokenWithDifferentTokenValue() {
        // given
        String storedTokenId = "1";
        String storedTokenValue = "middleBearPk";
        String givenTokenValue = "ozJoanne";
        RefreshToken storedRefreshToken = new RefreshToken(storedTokenId, storedTokenValue, TIME_TO_LIVE);

        given(jwtTokenProvider.getStringIdFromPayLoad(givenTokenValue, JwtTokenType.REFRESH_TOKEN)).willReturn(storedTokenId);
        given(refreshTokenRepository.findById(storedTokenId)).willReturn(Optional.of(storedRefreshToken));

        // when, then
        assertThatThrownBy(() -> authService.validateRefreshToken(givenTokenValue))
                .isExactlyInstanceOf(TokenNotValidException.class);
    }

    @Test
    @DisplayName("리프레시 토큰 삭제 - 성공")
    void removeRefreshToken() {
        // given
        String id = "1";
        String tokenValue = "middleBearPk";
        RefreshToken storedRefreshToken = new RefreshToken(id, tokenValue, TIME_TO_LIVE);

        given(jwtTokenProvider.getStringIdFromPayLoad(tokenValue, JwtTokenType.REFRESH_TOKEN)).willReturn(id);
        given(refreshTokenRepository.findById(id)).willReturn(Optional.of(storedRefreshToken));

        // when
        authService.removeRefreshToken(tokenValue);

        // then
        then(jwtTokenProvider).should(times(2)).getStringIdFromPayLoad(tokenValue, JwtTokenType.REFRESH_TOKEN);
        then(refreshTokenRepository).should(times(1)).findById(id);
        then(refreshTokenRepository).should(times(1)).deleteById(id);
    }

    @Test
    @DisplayName("리프레시 토큰 삭제 - 실패, 유효한 JWT 토큰이지만 레디스에 존재하지 않는 토큰인 경우")
    void removeRefreshTokenWhenNotStoredToken() {
        // given
        String id = "1";
        String storedTokenValue = "middleBearPk";
        RefreshToken storedRefreshToken = new RefreshToken(id, storedTokenValue, TIME_TO_LIVE);
        String notStoredTokenValue = "notStoredTokenValue";

        given(jwtTokenProvider.getStringIdFromPayLoad(notStoredTokenValue, JwtTokenType.REFRESH_TOKEN)).willReturn(id);
        given(refreshTokenRepository.findById(id)).willReturn(Optional.of(storedRefreshToken));

        // when
        authService.removeRefreshToken(notStoredTokenValue);

        // then
        then(jwtTokenProvider).should(times(1)).getStringIdFromPayLoad(notStoredTokenValue, JwtTokenType.REFRESH_TOKEN);
        then(refreshTokenRepository).should(times(1)).findById(id);
        then(refreshTokenRepository).should(times(0)).deleteById(id);
    }

    @DisplayName("토큰으로 appUser를 찾는다. - 성공, 관리자가 아닌 경우")
    @Test
    void findAppUserByToken() {
        // given
        String credential = "credential";
        given(jwtTokenProvider.getIdFromPayLoad(credential, JwtTokenType.ACCESS_TOKEN)).willReturn(1L);
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
        given(jwtTokenProvider.getIdFromPayLoad(credential, JwtTokenType.ACCESS_TOKEN)).willReturn(1L);
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
        willThrow(TokenNotValidException.class)
                .given(jwtTokenProvider).validateToken(credential, JwtTokenType.ACCESS_TOKEN);

        // when
        assertThatThrownBy(() -> authService.findAppUserByToken(credential))
                .isInstanceOf(TokenNotValidException.class);
    }
}
