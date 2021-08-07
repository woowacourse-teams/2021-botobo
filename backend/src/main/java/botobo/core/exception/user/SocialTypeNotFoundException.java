package botobo.core.exception.user;

import botobo.core.exception.NotFoundException;

public class SocialTypeNotFoundException extends NotFoundException {
    public SocialTypeNotFoundException() {
        super("존재하지 않는 Social Type 입니다.");
    }

    public SocialTypeNotFoundException(String message) {
        super(message);
    }
}
