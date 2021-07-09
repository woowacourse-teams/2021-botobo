package botobo.core.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminCardRequest {
    private String question;
    private Long categoryId;

    public AdminCardRequest(String question, Long categoryId) {
        this.question = question;
        this.categoryId = categoryId;
    }
}
