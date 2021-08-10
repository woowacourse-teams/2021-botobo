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
public class SimpleUserResponse {

    private Long id;
    private String name;

    public static List<SimpleUserResponse> listOf(List<User> users) {
        return users.stream()
                .map(SimpleUserResponse::of)
                .collect(Collectors.toList());
    }

    public static SimpleUserResponse of(User user) {
        return SimpleUserResponse.builder()
                .id(user.getId())
                .name(user.getUserName())
                .build();
    }
}
