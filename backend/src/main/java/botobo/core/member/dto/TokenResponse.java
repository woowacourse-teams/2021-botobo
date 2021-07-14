package botobo.core.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {

    private String accessToken;

    public TokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
