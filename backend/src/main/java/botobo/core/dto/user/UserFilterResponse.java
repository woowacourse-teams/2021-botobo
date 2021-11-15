package botobo.core.dto.user;

import botobo.core.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilterResponse implements Serializable {
    private Long id;
    private String name;

    public static UserFilterResponse of(User user) {
        return UserFilterResponse.builder()
                .id(user.getId())
                .name(user.getUserName())
                .build();
    }

    public static List<UserFilterResponse> listOf(List<User> users) {
        return users.stream()
                .map(UserFilterResponse::of)
                .collect(Collectors.toList());
    }
}
