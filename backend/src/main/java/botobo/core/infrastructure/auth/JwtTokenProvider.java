package botobo.core.infrastructure.auth;

import botobo.core.exception.auth.TokenExpirationException;
import botobo.core.exception.auth.TokenNotValidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class JwtTokenProvider {

    private final JwtTokenInfo jwtAccessTokenInfo;
    private final JwtTokenInfo jwtRefreshTokenInfo;

    public JwtTokenProvider(JwtAccessTokenInfo jwtAccessTokenInfo, JwtRefreshTokenInfo jwtRefreshTokenInfo) {
        this.jwtAccessTokenInfo = jwtAccessTokenInfo;
        this.jwtRefreshTokenInfo = jwtRefreshTokenInfo;
    }

    public String createAccessToken(Long id) {
        return createToken(id, jwtAccessTokenInfo.getValidityInMilliseconds(), jwtAccessTokenInfo.getSecretKey());
    }

    public String createRefreshToken(Long id) {
        return createToken(id, jwtRefreshTokenInfo.getValidityInMilliseconds(), jwtRefreshTokenInfo.getSecretKey());
    }

    public String createRefreshToken(Long id, Long validityInMilliseconds) {
        return createToken(id, validityInMilliseconds, jwtRefreshTokenInfo.getSecretKey());
    }

    private String createToken(Long id, Long validityTime, String secretKey) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityTime);

        return Jwts.builder()
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getStringIdFromPayLoad(String token, JwtTokenType jwtTokenType) {
        return getIdFromPayLoad(token, jwtTokenType).toString();
    }

    public Long getIdFromPayLoad(String token, JwtTokenType jwtTokenType) {
        Claims claims = getClaims(token, jwtTokenType);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token, JwtTokenType jwtTokenType) {
        return Jwts.parser()
                .setSigningKey(secretKey(jwtTokenType))
                .parseClaimsJws(token)
                .getBody();
    }

    public String secretKey(JwtTokenType jwtTokenType) {
        if (jwtAccessTokenInfo.supports(jwtTokenType)) {
            return jwtAccessTokenInfo.getSecretKey();
        }
        return jwtRefreshTokenInfo.getSecretKey();
    }

    public void validateToken(String token, JwtTokenType jwtTokenType) {
        try {
            Objects.requireNonNull(token);
            Jwts.parser()
                    .setSigningKey(secretKey(jwtTokenType))
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new TokenExpirationException();
        } catch (NullPointerException | JwtException | IllegalArgumentException e) {
            throw new TokenNotValidException();
        }
    }

    public Long getJwtRefreshTokenTimeToLive() {
        return jwtRefreshTokenInfo.getValidityInSeconds();
    }
}
