package botobo.core.exception.user;

import botobo.core.exception.common.ConflictException;

public class UserNameDuplicatedException extends ConflictException {
    public UserNameDuplicatedException() {
        super("회원 이름은 중복될 수 없습니다.");
    }

    public UserNameDuplicatedException(String name) {
        super(String.format("%s(은)는 이미 존재합니다.", name));
    }
}
