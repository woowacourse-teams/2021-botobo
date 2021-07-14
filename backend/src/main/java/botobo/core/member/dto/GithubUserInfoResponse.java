package botobo.core.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubUserInfoResponse {

    @JsonProperty("login")
    private String userName;
    private Long id;
    @JsonProperty("avatar_url")
    private String profileUrl;

    public GithubUserInfoResponse(String userName, Long id, String profileUrl) {
        this.userName = userName;
        this.id = id;
        this.profileUrl = profileUrl;
    }
}
