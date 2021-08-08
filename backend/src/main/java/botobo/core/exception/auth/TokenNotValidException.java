package botobo.core.exception.auth;

import botobo.core.exception.common.UnAuthorizedException;

public class TokenNotValidException extends UnAuthorizedException {
    public TokenNotValidException() {
        super("토큰이 유효하지 않습니다.");
    }
}
