package botobo.core.dto.user;

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
    private String profileUrl;

}
