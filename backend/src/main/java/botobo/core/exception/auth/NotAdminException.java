package botobo.core.exception.auth;

import botobo.core.exception.common.ForbiddenException;

public class NotAdminException extends ForbiddenException {
    public NotAdminException() {
        super("Admin 권한이 아니기에 접근할 수 없습니다.");
    }
}
