package botobo.core.dto.auth;

import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
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
    @JsonProperty("id")
    private Long githubId;
    @JsonProperty("avatar_url")
    private String profileUrl;

    public User toUser() {
        return User.builder()
                .githubId(githubId)
                .userName(userName)
                .profileUrl(profileUrl)
                .role(Role.USER)
                .build();
    }

}
