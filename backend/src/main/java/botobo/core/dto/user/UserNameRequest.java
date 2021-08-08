package botobo.core.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class UserNameRequest {

    @NotNull(message = "회원명 중복 조회를 위해서는 이름이 필요합니다.")
    @Length(min= 1, max = 20, message = "이름은 최소 1자 이상, 최대 20자까지 입력 가능합니다.")
    private String userName;
}
