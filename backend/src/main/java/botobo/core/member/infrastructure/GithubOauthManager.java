package botobo.core.member.infrastructure;

import botobo.core.member.dto.GithubTokenRequest;
import botobo.core.member.dto.GithubTokenResponse;
import botobo.core.member.dto.GithubUserInfoResponse;
import botobo.core.member.dto.LoginRequest;
import botobo.core.member.exception.GithubApiFailedException;
import botobo.core.member.exception.UserProfileLoadFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubOauthManager {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String url;
    @Value("${github.url.profile}")
    private String profileUrl;

    public GithubUserInfoResponse getUserInfoFromGithub(LoginRequest loginRequest) {
        GithubTokenResponse githubTokenResponse = getAccessTokenFromGithub(loginRequest.getCode());
        final String accessToken = githubTokenResponse.getAccessToken();
        if (accessToken == null) {
            throw new GithubApiFailedException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);
        HttpEntity httpEntity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate
                    .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubUserInfoResponse.class)
                    .getBody();
        } catch (RestClientException e) {
            throw new UserProfileLoadFailedException();
        }
    }

    private GithubTokenResponse getAccessTokenFromGithub(String code) {
        RestTemplate restTemplate = new RestTemplate();
        GithubTokenRequest githubTokenRequest = GithubTokenRequest.builder()
                .code(code)
                .client_id(clientId)
                .client_secret(clientSecret)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<GithubTokenRequest> httpEntity = new HttpEntity<>(githubTokenRequest, httpHeaders);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, GithubTokenResponse.class).getBody();
    }
}
