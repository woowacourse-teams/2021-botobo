package botobo.core.domain.rank;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class SearchRankRepository {

    private static final String KEY = "SearchRank";

    private final RedisTemplate<String, SearchRank> searchRankRedisTemplate;
    private final ListOperations<String, SearchRank> listOperations;

    public SearchRankRepository(RedisTemplate<String, SearchRank> searchRankRedisTemplate) {
        this.searchRankRedisTemplate = searchRankRedisTemplate;
        this.listOperations = searchRankRedisTemplate.opsForList();
    }

    public void pushAll(Collection<SearchRank> searchRanks) {
        if (searchRanks.isEmpty()) {
            return;
        }
        listOperations.rightPushAll(KEY, searchRanks);
    }

    public List<SearchRank> findAll() {
        List<SearchRank> searchRanks = new ArrayList<>();
        long count = count();
        for (long i = 0; i < count; i++) {
            searchRanks.add(listOperations.index(KEY, i));
        }
        return searchRanks;
    }

    public Long count() {
        return listOperations.size(KEY);
    }

    public void deleteAll() {
        searchRankRedisTemplate.delete(KEY);
    }
}
