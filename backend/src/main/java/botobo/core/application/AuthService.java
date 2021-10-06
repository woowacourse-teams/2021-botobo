package botobo.core.application;

import botobo.core.domain.token.RefreshToken;
import botobo.core.domain.token.RefreshTokenRepository;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.exception.auth.NotAdminException;
import botobo.core.exception.auth.TokenNotValidException;
import botobo.core.exception.http.UnAuthorizedException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.infrastructure.auth.JwtTokenProvider;
import botobo.core.infrastructure.auth.JwtTokenType;
import botobo.core.infrastructure.auth.OauthManager;
import botobo.core.infrastructure.auth.OauthManagerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final OauthManagerFactory oauthManagerFactory;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, OauthManagerFactory oauthManagerFactory, RefreshTokenRepository refreshTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.oauthManagerFactory = oauthManagerFactory;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public TokenResponse createAccessToken(String socialType, LoginRequest loginRequest) {
        SocialType socialLoginType = SocialType.of(socialType);
        OauthManager oauthManager = oauthManagerFactory.findOauthMangerBySocialType(socialLoginType);
        User userInfo = oauthManager.getUserInfo(loginRequest.getCode());
        Optional<User> user = userRepository.findBySocialIdAndSocialType(userInfo.getSocialId(), socialLoginType);
        if (user.isPresent()) {
            return TokenResponse.of(jwtTokenProvider.createAccessToken(user.get().getId()));
        }
        User savedUser = userRepository.save(userInfo);
        return TokenResponse.of(jwtTokenProvider.createAccessToken(savedUser.getId()));
    }

    public TokenResponse renewAccessToken(Long id) {
        return TokenResponse.of(jwtTokenProvider.createAccessToken(id));
    }

    @Transactional
    public String createRefreshToken(Long id) {
        String refreshToken = jwtTokenProvider.createRefreshToken(id);
        Long timeToLive = jwtTokenProvider.getJwtRefreshTokenTimeToLive();
        RefreshToken saveRefreshToken = refreshTokenRepository.save(new RefreshToken(id, refreshToken, timeToLive));
        return saveRefreshToken.getTokenValue();
    }

    @Transactional
    public void removeRefreshToken(String refreshToken) {
        try {
            validateRefreshToken(refreshToken);
        } catch (UnAuthorizedException e) {
            return;
        }
        String id = jwtTokenProvider.getStringIdFromPayLoad(refreshToken, JwtTokenType.REFRESH_TOKEN);
        refreshTokenRepository.deleteById(id);
    }

    public Long extractIdByToken(String token, JwtTokenType tokenType) {
        return jwtTokenProvider.getIdFromPayLoad(token, tokenType);
    }

    public AppUser findAppUserByToken(String accessToken) {
        if (accessToken == null) {
            return AppUser.anonymous();
        }
        validateAccessToken(accessToken);
        Long userId = jwtTokenProvider.getIdFromPayLoad(accessToken, JwtTokenType.ACCESS_TOKEN);
        if (userRepository.existsByIdAndRole(userId, Role.ADMIN)) {
            return AppUser.admin(userId);
        }
        return AppUser.user(userId);
    }

    public void validateAdmin(String accessToken) {
        validateAccessToken(accessToken);
        User user = userRepository.findById(jwtTokenProvider.getIdFromPayLoad(accessToken, JwtTokenType.ACCESS_TOKEN))
                .orElseThrow(UserNotFoundException::new);
        if (!user.isAdmin()) {
            throw new NotAdminException();
        }
    }

    public void validateAccessToken(String accessToken) {
        jwtTokenProvider.validateToken(accessToken, JwtTokenType.ACCESS_TOKEN);
    }

    public void validateRefreshToken(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken, JwtTokenType.REFRESH_TOKEN);
        validateStoredRefreshToken(refreshToken);
    }

    private void validateStoredRefreshToken(String refreshToken) {
        String id = jwtTokenProvider.getStringIdFromPayLoad(refreshToken, JwtTokenType.REFRESH_TOKEN);
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(id)
                .orElseThrow(TokenNotValidException::new);
        if (!refreshToken.equals(storedRefreshToken.getTokenValue())) {
            throw new TokenNotValidException();
        }
    }
}
