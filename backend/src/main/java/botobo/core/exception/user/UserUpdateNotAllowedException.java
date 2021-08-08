package botobo.core.exception.user;

import botobo.core.exception.common.ForbiddenException;

public class UserUpdateNotAllowedException extends ForbiddenException {
    public UserUpdateNotAllowedException() {
        super("식별값이 다르므로 내 정보를 수정할 권한이 없습니다.");
    }

    public UserUpdateNotAllowedException(String message) {
        super(message);
    }
}
