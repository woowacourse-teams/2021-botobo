package botobo.core.dto.card;

import botobo.core.domain.card.Card;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class CardUpdateRequest {

    @NotBlank(message = "C002")
    @Length(max = 2000, message = "C003")
    private String question;

    @NotEmpty(message = "C004")
    @Length(max = 2000, message = "C005")
    private String answer;

    @NotNull(message = "C006")
    @Positive(message = "C007")
    private Long workbookId;

    @NotNull(message = "C008")
    @PositiveOrZero(message = "C009")
    private Integer encounterCount;

    @NotNull(message = "C010")
    private Boolean bookmark;

    @NotNull(message = "C011")
    private Boolean nextQuiz;

    public Card toCard() {
        return Card.builder()
                .question(question)
                .answer(answer)
                .encounterCount(encounterCount)
                .bookmark(bookmark)
                .nextQuiz(nextQuiz)
                .build();
    }
}
