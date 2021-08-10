package botobo.core.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OauthTokenRequest {

    private String code;
    private String client_id;
    private String client_secret;
}
