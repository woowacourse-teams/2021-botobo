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

    @NotNull(message = "W003")
    @PositiveOrZero(message = "W004")
    private Long id;

    @NotBlank(message = "W005")
    @Length(max = 20, message = "W006")
    private String name;
}
