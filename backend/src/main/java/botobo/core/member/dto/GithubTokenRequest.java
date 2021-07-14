package botobo.core.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubTokenRequest {

    private String code;
    private String client_id;
    private String client_secret;

    public GithubTokenRequest(String code, String client_id, String client_secret) {
        this.code = code;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }
}
