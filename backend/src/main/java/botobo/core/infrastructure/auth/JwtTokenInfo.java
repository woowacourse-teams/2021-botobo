package botobo.core.infrastructure.auth;

public interface JwtTokenInfo {
    String getSecretKey();

    Long getValidityInMilliseconds();

    Long getValidityInSeconds();

    boolean supports(JwtTokenType jwtTokenType);
}
