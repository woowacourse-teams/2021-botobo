package botobo.core.auth.exception;

import botobo.core.common.exception.ForbiddenException;

public class NotAdminException extends ForbiddenException {
    public NotAdminException() {
        super("Admin 권한이 아니기에 접근할 수 없습니다.");
    }
}
