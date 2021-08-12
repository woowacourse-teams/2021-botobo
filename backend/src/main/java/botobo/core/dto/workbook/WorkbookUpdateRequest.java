package botobo.core.dto.workbook;

import botobo.core.domain.tag.Tags;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class WorkbookUpdateRequest {

    @NotBlank(message = "W002")
    @Length(max = 30, message = "W001")
    private String name;

    @NotNull(message = "W009")
    private Boolean opened;

    @NotNull(message = "W010")
    @PositiveOrZero(message = "W011")
    private Integer cardCount;

    @NotNull(message = "W012")
    @PositiveOrZero(message = "W013")
    private Integer heartCount;

    @NotNull(message = "W008")
    @Valid
    private List<TagRequest> tags;

    public Workbook toWorkbookWithTags(Tags tags) {
        return Workbook.builder()
                .name(name)
                .opened(opened)
                .tags(tags)
                .build();
    }
}
