package botobo.core.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AdminCardRequest {

    @NotBlank(message = "질문은 필수 입력값입니다.")
    private String question;

    @NotBlank(message = "답변은 필수 입력값입니다.")
    private String answer;

    @NotNull(message = "카드가 포함될 문제집 아이디는 필수 입력값입니다.")
    private Long workbookId;


    public AdminCardRequest(String question, String answer, Long workbookId) {
        this.question = question;
        this.answer = answer;
        this.workbookId = workbookId;
    }
}
