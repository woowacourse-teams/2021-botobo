package botobo.core.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    private String code;

    public LoginRequest(String code) {
        this.code = code;
    }
}