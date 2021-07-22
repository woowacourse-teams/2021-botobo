package botobo.core.auth.exception;

import botobo.core.common.exception.UnauthorizedException;

public class GithubApiFailedException extends UnauthorizedException {

    public GithubApiFailedException() {
        super("GithubAccessToken을 받아오는데 실패했습니다.");
    }
}
