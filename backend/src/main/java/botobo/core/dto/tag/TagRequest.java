package botobo.core.dto.tag;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class TagRequest {

    @NotNull(message = "태그 아이디는 필수 입력값입니다.")
    @PositiveOrZero(message = "태그 아이디는 0이상의 숫자입니다.")
    private Long id;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Length(max = 20, message = "태그는 최대 20자까지 입력 가능합니다.")
    private String name;
}
