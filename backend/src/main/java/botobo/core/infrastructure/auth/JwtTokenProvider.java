package botobo.core.infrastructure.auth;

import botobo.core.exception.auth.TokenExpirationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.access-token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.access-token.expire-length}")
    private Long accessValidityInMilliseconds;
    @Value("${security.jwt.refresh-token.expire-length}")
    private Long refreshValidityInMilliseconds;

    public String createAccessToken(Long id) {
        return createToken(id, accessValidityInMilliseconds);
    }

    public String createRefreshToken(Long id) {
        return createToken(id, refreshValidityInMilliseconds);
    }

    private String createToken(Long id, Long validityTime) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityTime);

        return Jwts.builder()
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long getIdFromPayLoad(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Long.class);
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenExpirationException();
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
