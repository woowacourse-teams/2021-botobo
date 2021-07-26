package botobo.core.quiz.dto.workbook;

import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.dto.CardSimpleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkbookCardResponse {

    private Long workbookId;
    private String workbookName;
    private List<CardSimpleResponse> cards;

    public static WorkbookCardResponse of(Workbook workbook) {
        List<CardSimpleResponse> cardSimpleResponses = CardSimpleResponse.listOf(workbook.getCardsAsList());
        return WorkbookCardResponse.builder()
                .workbookId(workbook.getId())
                .workbookName(workbook.getName())
                .cards(cardSimpleResponses)
                .build();
    }
}
