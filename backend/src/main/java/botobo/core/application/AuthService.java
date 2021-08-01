package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.LoginRequest;
import botobo.core.dto.auth.TokenResponse;
import botobo.core.exception.auth.NotAdminException;
import botobo.core.exception.auth.TokenNotValidException;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.infrastructure.GithubOauthManager;
import botobo.core.infrastructure.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final GithubOauthManager githubOauthManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthService(GithubOauthManager githubOauthManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.githubOauthManager = githubOauthManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Transactional
    public TokenResponse createToken(LoginRequest loginRequest) {
        GithubUserInfoResponse userInfo = githubOauthManager.getUserInfoFromGithub(loginRequest);
        Optional<User> user = userRepository.findByGithubId(userInfo.getGithubId());
        if (user.isPresent()) {
            return TokenResponse.of(jwtTokenProvider.createToken(user.get().getId()));
        }
        User savedUser = userRepository.save(userInfo.toUser());
        return TokenResponse.of(jwtTokenProvider.createToken(savedUser.getId()));
    }

    public AppUser findAppUserByToken(String credentials) {
        if (credentials == null) {
            return AppUser.anonymous();
        }
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
