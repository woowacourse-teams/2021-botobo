package botobo.core.dto.auth;

import botobo.core.domain.user.Role;
import botobo.core.domain.user.SocialType;
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
public class GoogleUserInfoResponse implements UserInfoResponse {

    @JsonProperty("name")
    private String userName;
    @JsonProperty("sub")
    private String socialId;
    @JsonProperty("picture")
    private String profileUrl;

    @Override
    public User toUser() {
        return User.builder()
                .socialId(socialId)
                .userName(userName)
                .profileUrl(profileUrl)
                .role(Role.USER)
                .socialType(SocialType.GOOGLE)
                .build();
    }
}
