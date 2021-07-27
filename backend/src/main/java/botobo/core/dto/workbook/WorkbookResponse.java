package botobo.core.dto.workbook;

import botobo.core.domain.workbook.Workbook;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String name;
    private boolean opened;
    private int cardCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String author;

    //TODO: 메서드명 생각해봐야함
    public static WorkbookResponse convert(Workbook workbook) {
        return WorkbookResponse.builder()
                .id(workbook.getId())
                .name(workbook.getName())
                .cardCount(workbook.cardCount())
                .opened(workbook.isOpened())
                .build();
    }

    public static WorkbookResponse of(Workbook workbook) {
        return WorkbookResponse.builder()
                .id(workbook.getId())
                .name(workbook.getName())
                .cardCount(workbook.cardCount())
                .author(workbook.author())
                .opened(workbook.isOpened())
                .build();
    }

    public static List<WorkbookResponse> listOf(List<Workbook> workbooks) {
        return workbooks.stream()
                .map(WorkbookResponse::of)
                .collect(Collectors.toList());
    }
}
