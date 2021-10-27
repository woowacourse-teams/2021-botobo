package botobo.core.config.redis;

import botobo.core.domain.rank.SearchRank;
import botobo.core.domain.token.RefreshToken;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        stringRedisTemplate.setEnableTransactionSupport(true);
        return stringRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, SearchRank> searchRankRedisTemplate() {
        RedisTemplate<String, SearchRank> searchRankRedisTemplate = new RedisTemplate<>();
        searchRankRedisTemplate.setConnectionFactory(redisConnectionFactory());
        searchRankRedisTemplate.setEnableTransactionSupport(true);
        return searchRankRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate() {
        RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate = new RedisTemplate<>();
        refreshTokenRedisTemplate.setConnectionFactory(redisConnectionFactory());
        refreshTokenRedisTemplate.setEnableTransactionSupport(true);
        return refreshTokenRedisTemplate;
    }
}
