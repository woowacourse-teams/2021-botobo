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

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Length(max = 30, message = "이름은 최대 30자까지 입력 가능합니다.")
    private String name;

    @NotNull(message = "문제집 공개여부는 필수 입력값입니다.")
    private Boolean opened;

    @PositiveOrZero(message = "카드 개수는 0이상 입니다.")
    private int cardCount;

    @PositiveOrZero(message = "하트 수는 0이상 입니다.")
    private int heartCount;

    @NotNull(message = "문제집를 수정하려면 태그가 필요합니다.")
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
