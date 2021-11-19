package botobo.core.utils;

import botobo.core.domain.user.Role;
import botobo.core.domain.user.SocialType;
import botobo.core.domain.user.User;

public class UserFactory {

    public static User user(String socialId, String name, String profileUrl, SocialType socialType) {
        return User.builder()
                .socialId(socialId)
                .userName(name)
                .profileUrl(profileUrl)
                .role(Role.USER)
                .socialType(socialType)
                .build();
    }

}
