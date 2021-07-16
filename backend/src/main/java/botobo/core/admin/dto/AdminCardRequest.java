package botobo.core.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AdminCardRequest {

    @NotBlank(message = "질문은 필수 입력값입니다.")
    private String question;

    @NotNull(message = "카테고리의 아이디는 필수 입력값입니다.")
    private Long categoryId;

    public AdminCardRequest(String question, Long categoryId) {
        this.question = question;
        this.categoryId = categoryId;
    }
}
