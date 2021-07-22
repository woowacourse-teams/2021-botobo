package botobo.core.auth.exception;

import botobo.core.common.exception.UnauthorizedException;

public class UserProfileLoadFailedException extends UnauthorizedException {

    public UserProfileLoadFailedException() {
        super("Github에서 유저정보를 불러오는데 실패했습니다.");
    }
}
