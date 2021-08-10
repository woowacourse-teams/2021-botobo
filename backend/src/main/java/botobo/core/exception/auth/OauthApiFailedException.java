package botobo.core.exception.auth;

import botobo.core.exception.common.UnAuthorizedException;

public class OauthApiFailedException extends UnAuthorizedException {

    public OauthApiFailedException() {
        super("AccessToken을 받아오는데 실패했습니다.");
    }
}
