package botobo.core.quiz.dto;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.workbook.Workbook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizResponse {

    private Long id;
    private String question;
    private String answer;
    private String workbookName;

    public static QuizResponse of(Card card) {
        final Workbook workbook = card.getWorkbook();
        final String answer = card.getAnswer();
        return QuizResponse.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(answer)
                .workbookName(workbook.getName())
                .build();
    }
}
