package botobo.core.admin.dto;

import botobo.core.quiz.domain.workbook.Workbook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminWorkbookResponse {

    private Long id;
    private String name;

    public static AdminWorkbookResponse of(Workbook workbook) {
        return AdminWorkbookResponse.builder()
                .id(workbook.getId())
                .name(workbook.getName())
                .build();
    }
}
