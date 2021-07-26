package botobo.core.dto.card;

import botobo.core.domain.card.Card;
import botobo.core.domain.workbook.Workbook;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class CardUpdateRequest {

    @NotBlank(message = "카드를 업데이트하기 위해서는 질문이 필요합니다.")
    @Length(max = 255, message = "질문은 최대 255자까지 입력 가능합니다.")
    private String question;

    @NotBlank(message = "카드를 업데이트하기 위해서는 답변이 필요합니다.")
    @Length(max = 255, message = "답변은 최대 255자까지 입력 가능합니다.")
    private String answer;

    @NotNull
    @Positive
    private Long workbookId;

    @NotNull
    private Integer encounterCount;

    @NotNull(message = "카드를 업데이트하기 위해서는 북마크 정보가 필요합니다.")
    private Boolean bookmark;

    @NotNull(message = "카드를 업데이트하기 위해서는 또 보기 정보가 필요합니다.")
    private Boolean nextQuiz;

    public Card toCardWithWorkbook(Workbook workbook) {
        return Card.builder()
                .question(question)
                .answer(answer)
                .workbook(workbook)
                .encounterCount(encounterCount)
                .isBookmark(bookmark)
                .isNextQuiz(nextQuiz)
                .build();
    }
}
