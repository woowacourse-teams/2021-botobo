package botobo.core.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubUserInfoResponse {

    @JsonProperty("login")
    private String userName;
    private Long id;
    @JsonProperty("avatar_url")
    private String profileUrl;

}
