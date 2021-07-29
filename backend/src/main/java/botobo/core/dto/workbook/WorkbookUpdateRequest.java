package botobo.core.dto.workbook;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class WorkbookUpdateRequest {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Length(max = 30, message = "이름은 최대 30자까지 입력 가능합니다.")
    private String name;
    private boolean opened;
    @PositiveOrZero(message = "카드 개수는 0이상 입니다.")
    private int cardCount;
}
