package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.exception.auth.NotAdminException;
import botobo.core.exception.auth.TokenNotValidException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.infrastructure.auth.JwtTokenProvider;
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

    public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, OauthManagerFactory oauthManagerFactory) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.oauthManagerFactory = oauthManagerFactory;
    }

    @Transactional
    public TokenResponse createToken(String socialType, LoginRequest loginRequest) {
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

    public AppUser findAppUserByToken(String credentials) {
        if (credentials == null) {
            return AppUser.anonymous();
        }
        validateToken(credentials);
        Long userId = jwtTokenProvider.getIdFromPayLoad(credentials);
        if (userRepository.existsByIdAndRole(userId, Role.ADMIN)) {
            return AppUser.admin(userId);
        }
        return AppUser.user(userId);
    }

    public void validateAdmin(String credentials) {
        validateToken(credentials);
        User user = userRepository.findById(jwtTokenProvider.getIdFromPayLoad(credentials))
                .orElseThrow(UserNotFoundException::new);
        if (!user.isAdmin()) {
            throw new NotAdminException();
        }
    }

    public void validateToken(String credentials) {
        if (credentials == null || !jwtTokenProvider.isValidToken(credentials)) {
            throw new TokenNotValidException();
        }
    }
}
