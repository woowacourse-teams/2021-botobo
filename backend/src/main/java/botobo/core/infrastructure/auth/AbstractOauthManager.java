package botobo.core.infrastructure.auth;

import botobo.core.domain.user.User;
import botobo.core.dto.auth.OauthTokenRequest;
import botobo.core.dto.auth.OauthTokenResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.exception.auth.OauthApiFailedException;
import botobo.core.exception.auth.UserProfileLoadFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class AbstractOauthManager implements OauthManager {

    private final WebClient webClient = WebClient.create();

    abstract String getProfileUrl();

    abstract Class<? extends UserInfoResponse> getResponseType();

    abstract OauthTokenRequest getOauthTokenRequest(String code);

    abstract String getUrl();

    @Override
    public User getUserInfo(String code) {
        OauthTokenResponse oauthTokenResponse = getAccessToken(code);
        Mono<UserInfoResponse> userInfoResponseMono = webClient
                .get()
                .uri(getProfileUrl())
                .headers(headers -> headers.setBearerAuth(oauthTokenResponse.getAccessToken()))
                .exchangeToMono(response -> {
                    if (!response.statusCode().equals(HttpStatus.OK)) {
                        throw new UserProfileLoadFailedException();
                    }
                    return response.bodyToMono(getResponseType());
                });
        UserInfoResponse userInfoResponse = userInfoResponseMono.block();
        validateNotNull(userInfoResponse);
        return userInfoResponse.toUser();
    }

    private void validateNotNull(UserInfoResponse userInfoResponse) {
        if (userInfoResponse == null) {
            throw new UserProfileLoadFailedException();
        }
    }

    public OauthTokenResponse getAccessToken(String code) {
        OauthTokenRequest oauthTokenRequest = getOauthTokenRequest(code);
        Mono<OauthTokenResponse> oauthTokenResponseMono = webClient
                .post()
                .uri(getUrl())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(oauthTokenRequest)
                .exchangeToMono(response -> {
                    if (!response.statusCode().equals(HttpStatus.OK)) {
                        throw new OauthApiFailedException();
                    }
                    return response.bodyToMono(OauthTokenResponse.class);
                });
        OauthTokenResponse oauthTokenResponse = oauthTokenResponseMono.block();
        validateNotNull(oauthTokenResponse);
        String accessToken = oauthTokenResponse.getAccessToken();
        validateNotNull(accessToken);
        return oauthTokenResponse;
    }

    private void validateNotNull(OauthTokenResponse oauthTokenResponse) {
        if (oauthTokenResponse == null) {
            throw new OauthApiFailedException();
        }
    }

    private void validateNotNull(String accessToken) {
        if (accessToken == null) {
            throw new OauthApiFailedException();
        }
    }
}
