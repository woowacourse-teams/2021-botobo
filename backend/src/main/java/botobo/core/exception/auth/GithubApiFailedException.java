package botobo.core.exception.auth;

import botobo.core.exception.common.UnAuthorizedException;

public class GithubApiFailedException extends UnAuthorizedException {

    public GithubApiFailedException() {
        super("GithubAccessToken을 받아오는데 실패했습니다.");
    }
}
