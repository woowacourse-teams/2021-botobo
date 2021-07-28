package botobo.core.exception;

public class NotAuthorException extends ForbiddenException {
    public NotAuthorException() {
        super("작성자가 아니므로 권한이 없습니다.");
    }
}
