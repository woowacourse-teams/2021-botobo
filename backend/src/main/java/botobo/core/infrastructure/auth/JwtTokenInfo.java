package botobo.core.infrastructure.auth;

public interface JwtTokenInfo {
    String getSecretKey();

    Long getValidityInMilliseconds();

    boolean supports(JwtTokenType jwtTokenType);
}
