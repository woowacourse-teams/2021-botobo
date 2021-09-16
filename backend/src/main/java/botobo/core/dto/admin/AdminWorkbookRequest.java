package botobo.core.dto.admin;

import botobo.core.domain.workbook.Workbook;
import botobo.core.dto.tag.TagRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class AdminWorkbookRequest {

    @NotBlank(message = "W002")
    @Length(max = 30, message = "W001")
    private String name;
    private boolean opened = true;

    @Builder.Default
    @Valid
    private List<TagRequest> tags = new ArrayList<>();

    public AdminWorkbookRequest(String name) {
        this.name = name;
    }

    public AdminWorkbookRequest(String name, boolean opened) {
        this.name = name;
        this.opened = opened;
    }

    public Workbook toWorkbook() {
        return Workbook.builder()
                .name(name)
                .opened(true)
                .deleted(false)
                .opened(opened)
                .build();
    }
}
