package botobo.core.infrastructure;

import botobo.core.domain.user.SocialType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OauthManagerFactory {

    private final List<OauthManager> oauthManagers;

    public OauthManagerFactory(List<OauthManager> oauthManagers) {
        this.oauthManagers = oauthManagers;
    }

    public OauthManager findOauthMangerBySocialType(SocialType socialType) {
        return oauthManagers.stream()
                .filter(oauthManager -> oauthManager.isSameSocialType(socialType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 SocialType 입니다."));
    }
}
