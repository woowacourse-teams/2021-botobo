package botobo.core.infrastructure.auth;

import botobo.core.exception.auth.TokenExpirationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtTokenInfo jwtAccessTokenInfo, jwtRefreshTokenInfo;

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

    public Long getIdFromPayLoad(String token, JwtTokenType jwtTokenType) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey(jwtTokenType))
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Long.class);
    }

    public String secretKey(JwtTokenType jwtTokenType) {
        if (jwtAccessTokenInfo.supports(jwtTokenType)) {
            return jwtAccessTokenInfo.getSecretKey();
        }
        return jwtRefreshTokenInfo.getSecretKey();
    }

    public boolean isValidToken(String token, JwtTokenType jwtTokenType) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey(jwtTokenType))
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenExpirationException();
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
