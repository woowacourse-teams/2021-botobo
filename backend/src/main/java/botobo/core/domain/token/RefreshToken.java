package botobo.core.domain.token;


import lombok.Getter;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@Getter
public class RefreshToken implements Serializable {

    private String id;
    private String tokenValue;

    @TimeToLive
    private Long timeToLive;

    public RefreshToken(Long id, String tokenValue, Long timeToLive) {
        this(String.valueOf(id), tokenValue, timeToLive);
    }

    public RefreshToken(String id, String tokenValue, Long timeToLive) {
        this.id = id;
        this.tokenValue = tokenValue;
        this.timeToLive = timeToLive;
    }
}
