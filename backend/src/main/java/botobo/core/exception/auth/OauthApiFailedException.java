package botobo.core.exception.auth;

import botobo.core.exception.UnauthorizedException;

public class OauthApiFailedException extends UnauthorizedException {

    public OauthApiFailedException() {
        super("AccessToken을 받아오는데 실패했습니다.");
    }
}
