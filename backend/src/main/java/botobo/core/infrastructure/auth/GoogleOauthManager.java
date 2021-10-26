package botobo.core.infrastructure.auth;

import botobo.core.domain.user.SocialType;
import botobo.core.dto.auth.GoogleTokenRequest;
import botobo.core.dto.auth.GoogleUserInfoResponse;
import botobo.core.dto.auth.OauthTokenRequest;
import botobo.core.dto.auth.UserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleOauthManager extends AbstractOauthManager {

    @Value("${google.client.id}")
    private String clientId;
    @Value("${google.client.secret}")
    private String clientSecret;
    @Value("${google.client.redirect-uri}")
    private String redirectUri;
    @Value("${google.client.grant-type}")
    private String grantType;
    @Value("${google.url.access-token}")
    private String url;
    @Value("${google.url.profile}")
    private String profileUrl;

    @Override
    public boolean isSameSocialType(SocialType socialType) {
        return socialType == SocialType.GOOGLE;
    }

    @Override
    protected String getProfileUrl() {
        return profileUrl;
    }

    @Override
    protected Class<? extends UserInfoResponse> getResponseType() {
        return GoogleUserInfoResponse.class;
    }

    @Override
    protected OauthTokenRequest getOauthTokenRequest(String code) {
        return GoogleTokenRequest.builder()
                .code(code)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .grantType(grantType)
                .build();
    }

    @Override
    protected String getUrl() {
        return url;
    }
}
