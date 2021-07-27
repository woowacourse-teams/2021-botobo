package botobo.core.dto.workbook;

import botobo.core.domain.workbook.Workbook;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.card.CardSimpleResponse;
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
    private List<CardResponse> cards;

    public static WorkbookCardResponse of(Workbook workbook) {
        List<CardResponse> cardResponses = CardResponse.listOf(workbook.getCardsAsList());
        return WorkbookCardResponse.builder()
                .workbookId(workbook.getId())
                .workbookName(workbook.getName())
                .cards(cardResponses)
                .build();
    }
}
