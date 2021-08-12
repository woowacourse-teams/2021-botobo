package botobo.core.dto.user;

import botobo.core.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class UserUpdateRequest {

    @ValidUserName
    private String userName;

    @NotBlank(message = "U007")
    private String profileUrl;

    @NotNull(message = "U008")
    @Length(max = 255, message = "U009")
    private String bio;

    public User toUser() {
        return User.builder()
                .userName(userName)
                .profileUrl(profileUrl)
                .bio(bio)
                .build();
    }
}
