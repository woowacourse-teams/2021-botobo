package botobo.core.admin.dto;

import botobo.core.quiz.domain.workbook.Workbook;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminWorkbookResponse {

    private Long id;
    private String name;

    @Builder
    private AdminWorkbookResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AdminWorkbookResponse of(Workbook workbook) {
        return new AdminWorkbookResponse(
                workbook.getId(),
                workbook.getName()
        );
    }
}
