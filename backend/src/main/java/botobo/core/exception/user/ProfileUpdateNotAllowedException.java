package botobo.core.exception.user;

import botobo.core.exception.common.ForbiddenException;

public class ProfileUpdateNotAllowedException extends ForbiddenException {
    public ProfileUpdateNotAllowedException() {
        super("프로필 이미지 수정은 불가합니다.");
    }

    public ProfileUpdateNotAllowedException(String message) {
        super(message);
    }
}
