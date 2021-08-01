package botobo.core.dto.workbook;

import botobo.core.domain.workbook.Workbook;
import botobo.core.dto.card.CardResponse;
import botobo.core.dto.tag.TagResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class WorkbookCardResponse {

    private Long workbookId;
    private String workbookName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cardCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TagResponse> tagResponses;
    private List<CardResponse> cards;

    public static WorkbookCardResponse ofUserWorkbook(Workbook workbook) {
        List<CardResponse> cardResponses = CardResponse.listOf(workbook.getCards());
        return WorkbookCardResponse.builder()
                .workbookId(workbook.getId())
                .workbookName(workbook.getName())
                .cards(cardResponses)
                .build();
    }

    public static WorkbookCardResponse ofOpenedWorkbook(Workbook workbook) {
        List<CardResponse> cardResponses = CardResponse.listOfSimple(workbook.getCards());
        List<TagResponse> tagResponses = TagResponse.listOf(workbook.tags());
        return WorkbookCardResponse.builder()
                .workbookId(workbook.getId())
                .workbookName(workbook.getName())
                .cardCount(workbook.cardCount())
                .tagResponses(tagResponses)
                .cards(cardResponses)
                .build();
    }
}
