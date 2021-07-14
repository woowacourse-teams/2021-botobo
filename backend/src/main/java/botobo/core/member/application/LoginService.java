package botobo.core.member.application;

import botobo.core.member.dto.GithubUserInfoResponse;
import botobo.core.member.dto.LoginRequest;
import botobo.core.member.dto.TokenResponse;
import botobo.core.member.infrastructure.GithubOauthManager;
import botobo.core.member.infrastructure.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final GithubOauthManager githubOauthManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(GithubOauthManager githubOauthManager, JwtTokenProvider jwtTokenProvider) {
        this.githubOauthManager = githubOauthManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        GithubUserInfoResponse userInfoResponse = githubOauthManager.getUserInfoFromGithub(loginRequest);
        // TODO: 회원 존재하는지 확인하고 Long id를 받아온다.
        return TokenResponse.of(jwtTokenProvider.createToken(1L));
    }
}
