package botobo.core.infrastructure.auth;

import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;

public interface OauthManager {
    User getUserInfo(String code);

    boolean isSameSocialType(SocialType socialType);
}
