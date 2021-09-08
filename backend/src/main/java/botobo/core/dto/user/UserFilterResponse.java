package botobo.core.dto.user;

import botobo.core.domain.user.User;
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
public class UserFilterResponse {
    private Long id;
    private String userName;

    public static UserFilterResponse of(User user) {
        return UserFilterResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .build();
    }

    public static List<UserFilterResponse> listOf(List<User> users) {
        return users.stream()
                .map(UserFilterResponse::of)
                .collect(Collectors.toList());
    }
}
