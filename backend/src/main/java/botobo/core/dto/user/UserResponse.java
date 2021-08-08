package botobo.core.dto.user;

import botobo.core.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    //TODO bio 필드 추가
    private Long id;
    private String userName;
    private String bio;
    private String profileUrl;

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .bio(user.getBio())
                .profileUrl(user.getProfileUrl())
                .build();
    }
}
