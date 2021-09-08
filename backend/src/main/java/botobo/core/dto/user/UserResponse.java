package botobo.core.dto.user;

import botobo.core.domain.user.User;
import botobo.core.dto.tag.TagResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String userName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bio;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profileUrl;

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .bio(user.getBio())
                .profileUrl(user.getProfileUrl())
                .build();
    }

    public static List<UserResponse> listOf(List<User> users) {
        return users.stream()
                .map(UserResponse::of)
                .collect(Collectors.toList());
    }
}
