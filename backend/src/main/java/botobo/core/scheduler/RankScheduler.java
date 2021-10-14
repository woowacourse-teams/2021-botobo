package botobo.core.scheduler;

import botobo.core.application.rank.SearchRankService;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Conditional(RankSchedulerCondition.class)
@Profile({"local", "dev", "prod"})
@Component
public class RankScheduler {

    private final SearchRankService searchRankService;

    public RankScheduler(SearchRankService searchRankService) {
        this.searchRankService = searchRankService;
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void updateSearchRanks() {
        searchRankService.updateSearchRanks();
    }
}
