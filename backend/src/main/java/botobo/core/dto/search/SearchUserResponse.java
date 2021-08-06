package botobo.core.dto.search;

import botobo.core.domain.user.User;
import botobo.core.dto.user.UserResponse;
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
public class SearchUserResponse {

    private Long id;
    private String name;

    public static List<SearchUserResponse> listOf(List<User> users) {
        return users.stream()
                .map(SearchUserResponse::of)
                .collect(Collectors.toList());
    }

    public static SearchUserResponse of(User user) {
        return SearchUserResponse.builder()
                .id(user.getId())
                .name(user.getUserName())
                .build();
    }
}
