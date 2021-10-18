package botobo.core.domain.rank;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class SearchScoreRepository {

    public static final String KEY = "SearchScore";

    private final StringRedisTemplate stringRedisTemplate;
    private final ZSetOperations<String, String> setOperations;

    public SearchScoreRepository(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.setOperations = stringRedisTemplate.opsForZSet();
    }

    public Double score(String searchKeyword) {
        return setOperations.score(KEY, searchKeyword);
    }

    public void increaseScore(String searchKeyword) {
        setOperations.incrementScore(KEY, searchKeyword, 1.0);
    }

    public Long count() {
        return setOperations.zCard(KEY);
    }

    public void deleteAll() {
        stringRedisTemplate.delete(KEY);
    }

    public Set<String> findByScoreDesc(int limitCount) {
        return setOperations.reverseRangeByLex(
                KEY,
                RedisZSetCommands.Range.unbounded(),
                RedisZSetCommands.Limit.limit().count(limitCount)
        );
    }
}
