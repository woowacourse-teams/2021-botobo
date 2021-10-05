package botobo.core.infrastructure.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessTokenInfo implements JwtTokenInfo {

    @Value("${security.jwt.access-token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token.expire-length}")
    private Long validityInMilliseconds;

    @Override
    public String getSecretKey() {
        return secretKey;
    }

    @Override
    public Long getValidityInMilliseconds() {
        return validityInMilliseconds;
    }

    @Override
    public Long getValidityInSeconds() {
        return validityInMilliseconds / 1000;
    }

    @Override
    public boolean supports(JwtTokenType jwtTokenType) {
        return jwtTokenType.equals(JwtTokenType.ACCESS_TOKEN);
    }
}
