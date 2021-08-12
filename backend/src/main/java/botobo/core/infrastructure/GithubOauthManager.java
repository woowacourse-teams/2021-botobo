package botobo.core.infrastructure;

import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.dto.auth.GithubTokenRequest;
import botobo.core.dto.auth.GithubUserInfoResponse;
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
public class GithubOauthManager implements OauthManager {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String url;
    @Value("${github.url.profile}")
    private String profileUrl;

    @Override
    public User getUserInfo(String code) {
        OauthTokenResponse githubTokenResponse = getAccessToken(code);
        final String accessToken = githubTokenResponse.getAccessToken();
        if (accessToken == null) {
            throw new OauthApiFailedException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", githubTokenResponse.getTokenType() + " " + accessToken);
        HttpEntity httpEntity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            UserInfoResponse githubUserInfoResponse = restTemplate
                    .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubUserInfoResponse.class)
                    .getBody();
            return githubUserInfoResponse.toUser();
        } catch (RestClientException e) {
            throw new UserProfileLoadFailedException();
        }
    }

    @Override
    public boolean isSameSocialType(SocialType socialType) {
        return socialType == SocialType.GITHUB;
    }

    @Override
    public OauthTokenResponse getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        GithubTokenRequest githubTokenRequest = GithubTokenRequest.builder()
                .code(code)
                .client_id(clientId)
                .client_secret(clientSecret)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<GithubTokenRequest> httpEntity = new HttpEntity<>(githubTokenRequest, httpHeaders);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, OauthTokenResponse.class).getBody();
    }
}
