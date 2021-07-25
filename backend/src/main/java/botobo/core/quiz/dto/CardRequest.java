package botobo.core.quiz.dto;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.workbook.Workbook;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class CardRequest {

    @NotBlank(message = "질문은 필수 입력값입니다.")
    @Length(max = 255, message = "질문은 최대 255자까지 입력 가능합니다.")
    private String question;

    @NotBlank(message = "답변은 필수 입력값입니다.")
    @Length(max = 255, message = "답변은 최대 255자까지 입력 가능합니다.")
    private String answer;

    @NotNull(message = "카드가 포함될 문제집 아이디는 필수 입력값입니다.")
    private Long workbookId;

    public Card toCardWithWorkbook(Workbook workbook) {
        return Card.builder()
                .question(question)
                .answer(answer)
                .workbook(workbook)
                .build();
    }
}
