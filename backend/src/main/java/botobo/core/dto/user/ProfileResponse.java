package botobo.core.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String profileUrl;

    public static ProfileResponse fromDefaultProfileImage() {
        // TODO 변경 예정
        return ProfileResponse.builder()
                .profileUrl("https://d1mlkr1uzdb8as.cloudfront.net/users/botobo.png")
                .build();
    }
}
