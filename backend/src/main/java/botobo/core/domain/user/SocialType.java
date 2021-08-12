package botobo.core.domain.user;

import botobo.core.exception.user.SocialTypeNotFoundException;

import java.util.Arrays;

public enum SocialType {
    GITHUB, GOOGLE;

    public static SocialType of(String input) {
        return Arrays.stream(values())
                .filter(socialType -> socialType.name().equals(input.toUpperCase()))
                .findFirst()
                .orElseThrow(SocialTypeNotFoundException::new);
    }
}
