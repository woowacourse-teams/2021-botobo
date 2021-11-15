package botobo.core.scheduler;

import botobo.core.application.rank.SearchRankService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile({"local", "dev", "prod"})
@Component
public class MemoryCacheScheduler {

    private final SearchRankService searchRankService;

    public MemoryCacheScheduler(SearchRankService searchRankService) {
        this.searchRankService = searchRankService;
    }

    @Scheduled(cron = "0 0/10 * * * *")
    void removeSearchRanksCache() {
        searchRankService.removeSearchRanksCache();
    }
}
