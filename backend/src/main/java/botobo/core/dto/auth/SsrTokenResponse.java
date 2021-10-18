package botobo.core.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsrTokenResponse {

    private String accessToken;
    private String refreshTokenCookieInfo;

    public static SsrTokenResponse of(String accessToken, String refreshTokenCookieInfo) {
        return SsrTokenResponse.builder()
                .accessToken(accessToken)
                .refreshTokenCookieInfo(refreshTokenCookieInfo)
                .build();
    }
}
