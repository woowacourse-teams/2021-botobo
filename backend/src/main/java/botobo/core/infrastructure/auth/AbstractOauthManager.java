package botobo.core.infrastructure.auth;

import botobo.core.domain.user.User;
import botobo.core.dto.auth.OauthTokenRequest;
import botobo.core.dto.auth.OauthTokenResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.exception.auth.OauthApiFailedException;
import botobo.core.exception.auth.UserProfileLoadFailedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractOauthManager implements OauthManager {

    private final RestTemplate restTemplate = new RestTemplate();

    abstract String getProfileUrl();

    abstract Class<? extends UserInfoResponse> getResponseType();

    abstract OauthTokenRequest getOauthTokenRequest(String code);

    abstract String getUrl();

    @Override
    public User getUserInfo(String code) {
        OauthTokenResponse oauthTokenResponse = getAccessToken(code);
        HttpEntity<Void> httpEntity = httpEntityWithAccessToken(oauthTokenResponse);
        try {
            UserInfoResponse userInfoResponse = restTemplate
                    .exchange(getProfileUrl(), HttpMethod.GET, httpEntity, getResponseType())
                    .getBody();
            validateNotNull(userInfoResponse);
            return userInfoResponse.toUser();
        } catch (RestClientException e) {
            throw new UserProfileLoadFailedException();
        }
    }

    public OauthTokenResponse getAccessToken(String code) {
        OauthTokenRequest oauthTokenRequest = getOauthTokenRequest(code);
        HttpEntity<OauthTokenRequest> httpEntity = httpEntityWithJsonMediaType(oauthTokenRequest);
        OauthTokenResponse oauthTokenResponse = restTemplate.exchange(getUrl(), HttpMethod.POST, httpEntity, OauthTokenResponse.class).getBody();
        if (oauthTokenResponse == null) {
            throw new OauthApiFailedException();
        }
        final String accessToken = oauthTokenResponse.getAccessToken();
        if (accessToken == null) {
            throw new OauthApiFailedException();
        }
        return oauthTokenResponse;
    }

    private HttpEntity<OauthTokenRequest> httpEntityWithJsonMediaType(OauthTokenRequest oauthTokenRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(oauthTokenRequest, httpHeaders);
    }

    private HttpEntity<Void> httpEntityWithAccessToken(OauthTokenResponse oauthTokenResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(
                "Authorization",
                oauthTokenResponse.getTokenType() + " " + oauthTokenResponse.getAccessToken()
        );
        return new HttpEntity<>(headers);
    }

    private void validateNotNull(UserInfoResponse userInfoResponse) {
        if (userInfoResponse == null) {
            throw new UserProfileLoadFailedException();
        }
    }
}
