package botobo.core.dto.auth;

import botobo.core.domain.user.User;

public interface UserInfoResponse {
    User toUser();
}
