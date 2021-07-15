package botobo.core.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubTokenRequest {

    private String code;
    private String client_id;
    private String client_secret;

}
