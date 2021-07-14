package botobo.core.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {

    private String accessToken;

    private TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
