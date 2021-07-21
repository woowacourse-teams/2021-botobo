package botobo.core.auth.application;

import botobo.core.auth.dto.GithubUserInfoResponse;
import botobo.core.auth.dto.LoginRequest;
import botobo.core.auth.dto.TokenResponse;
import botobo.core.auth.infrastructure.GithubOauthManager;
import botobo.core.auth.infrastructure.JwtTokenProvider;
import botobo.core.user.domain.User;
import botobo.core.user.domain.UserRepository;
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
}
