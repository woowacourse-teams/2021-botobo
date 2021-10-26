package botobo.core.infrastructure.auth;

import botobo.core.domain.user.SocialType;
import botobo.core.dto.auth.GithubTokenRequest;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.OauthTokenRequest;
import botobo.core.dto.auth.UserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubOauthManager extends AbstractOauthManager {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String url;
    @Value("${github.url.profile}")
    private String profileUrl;

    @Override
    public boolean isSameSocialType(SocialType socialType) {
        return socialType == SocialType.GITHUB;
    }

    @Override
    protected String getProfileUrl() {
        return profileUrl;
    }

    @Override
    protected Class<? extends UserInfoResponse> getResponseType() {
        return GithubUserInfoResponse.class;
    }

    @Override
    protected OauthTokenRequest getOauthTokenRequest(String code) {
        return GithubTokenRequest.builder()
                .code(code)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    @Override
    protected String getUrl() {
        return url;
    }
}
