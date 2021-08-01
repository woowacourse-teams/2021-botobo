package botobo.core.exception.auth;

import botobo.core.exception.UnauthorizedException;

public class TokenNotValidException extends UnauthorizedException {
    public TokenNotValidException() {
        super("토큰이 유효하지 않습니다.");
    }
}
