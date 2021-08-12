package botobo.core.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AdminCardRequest {

    @NotBlank(message = "C002")
    private String question;

    @NotBlank(message = "C004")
    private String answer;

    @NotNull(message = "C006")
    private Long workbookId;


    public AdminCardRequest(String question, String answer, Long workbookId) {
        this.question = question;
        this.answer = answer;
        this.workbookId = workbookId;
    }
}
