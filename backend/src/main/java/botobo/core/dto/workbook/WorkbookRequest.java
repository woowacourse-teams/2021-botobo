package botobo.core.dto.workbook;

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
public class WorkbookRequest {

    @NotBlank(message = "W002")
    @Length(max = 30, message = "W001")
    private String name;

    private boolean opened;

    @Builder.Default
    @Valid
    private List<TagRequest> tags = new ArrayList<>();

    public Workbook toWorkbook() {
        return Workbook.builder()
                .name(name)
                .opened(opened)
                .deleted(false)
                .build();
    }
}
