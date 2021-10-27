package botobo.core.domain.token;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenRepository {

    private static final String KEY = "RefreshToken";

    private final RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate;
    private final HashOperations<String, String, RefreshToken> hashOperations;

    public RefreshTokenRepository(RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate) {
        this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
        this.hashOperations = refreshTokenRedisTemplate.opsForHash();
    }

    public Optional<RefreshToken> findById(String id) {
        return Optional.ofNullable(hashOperations.get(KEY, id));
    }

    public RefreshToken save(RefreshToken refreshToken) {
        String id = refreshToken.getId();
        hashOperations.put(KEY, id, refreshToken);
        return refreshToken;
    }

    public void deleteById(String id) {
        hashOperations.delete(KEY, id);
    }

    public void deleteAll() {
        refreshTokenRedisTemplate.delete(KEY);
    }

    public boolean existsById(String id) {
        return hashOperations.hasKey(KEY, id);
    }
}
