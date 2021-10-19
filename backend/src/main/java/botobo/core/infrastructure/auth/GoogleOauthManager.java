package botobo.core.infrastructure.auth;

import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.dto.auth.GoogleTokenRequest;
import botobo.core.dto.auth.GoogleUserInfoResponse;
import botobo.core.dto.auth.OauthTokenResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.exception.auth.OauthApiFailedException;
import botobo.core.exception.auth.UserProfileLoadFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleOauthManager implements OauthManager {

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
    public User getUserInfo(String code) {
        OauthTokenResponse googleTokenResponse = getAccessToken(code);
        final String accessToken = googleTokenResponse.getAccessToken();
        if (accessToken == null) {
            throw new OauthApiFailedException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", googleTokenResponse.getTokenType() + " " + accessToken);
        HttpEntity httpEntity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            UserInfoResponse googleUserInfoResponse = restTemplate
                    .exchange(profileUrl, HttpMethod.GET, httpEntity, GoogleUserInfoResponse.class)
                    .getBody();
            return googleUserInfoResponse.toUser();
        } catch (RestClientException e) {
            throw new UserProfileLoadFailedException();
        }
    }

    @Override
    public boolean isSameSocialType(SocialType socialType) {
        return socialType == SocialType.GOOGLE;
    }

    @Override
    public OauthTokenResponse getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        GoogleTokenRequest googleTokenRequest = GoogleTokenRequest.builder()
                .code(code)
                .client_id(clientId)
                .client_secret(clientSecret)
                .redirect_uri(redirectUri)
                .grant_type(grantType)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<GoogleTokenRequest> httpEntity = new HttpEntity<>(googleTokenRequest, httpHeaders);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, OauthTokenResponse.class).getBody();
    }
}
