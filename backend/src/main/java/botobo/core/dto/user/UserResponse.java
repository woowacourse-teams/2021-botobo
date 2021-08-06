package botobo.core.dto.user;

import botobo.core.domain.user.User;
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
    private String profileUrl;

    public static List<UserResponse> listOfSearch(List<User> users) {
        return users.stream()
                .map(UserResponse::ofSearch)
                .collect(Collectors.toList());
    }

    public static UserResponse ofSearch(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .build();
    }
}
