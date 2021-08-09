package botobo.core.exception.auth;

import botobo.core.exception.common.UnAuthorizedException;

public class UserProfileLoadFailedException extends UnAuthorizedException {

    public UserProfileLoadFailedException() {
        super("유저정보를 불러오는데 실패했습니다.");
    }
}
