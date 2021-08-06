package botobo.core.exception.user;

import botobo.core.exception.common.ForbiddenException;

public class NotAuthorException extends ForbiddenException {
    public NotAuthorException() {
        super("작성자가 아니므로 권한이 없습니다.");
    }
}
