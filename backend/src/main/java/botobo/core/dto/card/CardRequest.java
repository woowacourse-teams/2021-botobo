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
public class CardRequest {

    @NotBlank(message = "C002")
    @Length(max = 2000, message = "C003")
    private String question;

    @NotBlank(message = "C004")
    @Length(max = 2000, message = "C005")
    private String answer;

    @NotNull(message = "C006")
    @Positive(message = "C007")
    private Long workbookId;

    public Card toCardWithWorkbook(Workbook workbook) {
        return Card.builder()
                .question(question)
                .answer(answer)
                .workbook(workbook)
                .build();
    }
}
