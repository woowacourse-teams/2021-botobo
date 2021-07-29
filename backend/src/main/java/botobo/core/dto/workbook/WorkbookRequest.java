package botobo.core.dto.workbook;

import botobo.core.domain.workbook.Workbook;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class WorkbookRequest {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Length(max = 30, message = "이름은 최대 30자까지 입력 가능합니다.")
    private String name;
    private boolean opened;

    public Workbook toWorkbook() {
        return Workbook.builder()
                .name(name)
                .opened(opened)
                .deleted(false)
                .build();
    }
}
