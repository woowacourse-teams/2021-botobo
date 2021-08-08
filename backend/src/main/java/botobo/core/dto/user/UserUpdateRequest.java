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

    @NotNull(message = "회원 정보를 수정하기 위해서는 이름이 필요합니다.")
    @Length(min = 1, max = 20, message = "이름은 최소 1자 이상, 최대 20자까지 입력 가능합니다.")
    private String userName;

    @NotBlank(message = "회원 정보를 수정하기 위해서는 프로필 사진이 필요합니다.")
    private String profileUrl;

    @NotNull(message = "회원 정보를 수정하기 위해서는 소개글은 최소 0자 이상이 필요합니다.")
    @Length(max = 255, message = "소개글은 최대 255자까지 가능합니다.")
    private String bio;

    public User toUser() {
        return User.builder()
                .userName(userName)
                .profileUrl(profileUrl)
                .bio(bio)
                .build();
    }
}
