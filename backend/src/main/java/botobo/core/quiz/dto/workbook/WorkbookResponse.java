package botobo.core.quiz.dto.workbook;

import botobo.core.quiz.domain.workbook.Workbook;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkbookResponse {

    private Long id;
    private String workbookName;
    private int cardCount;
    private String author;
    private boolean opened;

    public static WorkbookResponse of(Workbook workbook) {
        return WorkbookResponse.builder()
                .id(workbook.getId())
                .workbookName(workbook.getName())
                .cardCount(workbook.cardCount())
                .author(workbook.author())
                .opened(workbook.isPublic())
                .build();
    }

    public static List<WorkbookResponse> listOf(List<Workbook> workbooks) {
        return workbooks.stream()
                .map(WorkbookResponse::of)
                .collect(Collectors.toList());
    }
}
