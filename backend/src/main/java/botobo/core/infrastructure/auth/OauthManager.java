package botobo.core.infrastructure.auth;

import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;
import botobo.core.dto.auth.OauthTokenResponse;

public interface OauthManager {
    User getUserInfo(String code);

    boolean isSameSocialType(SocialType socialType);
}
