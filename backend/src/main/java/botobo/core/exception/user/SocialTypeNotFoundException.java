package botobo.core.exception.user;

import botobo.core.exception.common.NotFoundException;

public class SocialTypeNotFoundException extends NotFoundException {
    public SocialTypeNotFoundException() {
        super("존재하지 않는 소셜 로그인 방식입니다.");
    }

    public SocialTypeNotFoundException(String message) {
        super(message);
    }
}
