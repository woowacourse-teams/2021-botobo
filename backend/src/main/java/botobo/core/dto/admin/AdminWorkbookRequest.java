package botobo.core.dto.admin;

import botobo.core.domain.workbook.Workbook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AdminWorkbookRequest {

    @NotBlank(message = "문제집명은 필수 입력값입니다.")
    @Length(max = 30, message = "문제집명은 최소 1글자, 최대 30글자만 가능합니다.")
    private String name;
    private boolean opened = true;

    public AdminWorkbookRequest(String name) {
        this.name = name;
    }

    public AdminWorkbookRequest(String name, boolean isPublic) {
        this.name = name;
        this.opened = isPublic;
    }

    public Workbook toWorkbook() {
        return Workbook.builder()
                .name(name)
                .opened(true)
                .isDeleted(false)
                .opened(opened)
                .build();
    }
}
