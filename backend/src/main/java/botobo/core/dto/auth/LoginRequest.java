package botobo.core.dto.auth;

import botobo.core.domain.user.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String code;
    private SocialType socialType;

}