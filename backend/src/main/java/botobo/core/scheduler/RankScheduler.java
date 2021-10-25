package botobo.core.scheduler;

import botobo.core.application.WorkbookRankService;
import botobo.core.application.rank.SearchRankService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Profile({"local", "dev", "prod"})
@Conditional(RankSchedulerCondition.class)
@Component
public class RankScheduler {

    private final WorkbookRankService workbookRankService;
    private final SearchRankService searchRankService;

    public RankScheduler(WorkbookRankService workbookRankService, SearchRankService searchRankService) {
        this.workbookRankService = workbookRankService;
        this.searchRankService = searchRankService;
    }

    @Scheduled(cron = "0 0/10 * * * *")
    @CacheEvict("workbookRanks")
    public void updateWorkbookRanks() {
        workbookRankService.removeWorkbookRanksCache();
    }

    @Scheduled(cron = "0 9/10 * * * *")
    public void updateSearchRanks() {
        searchRankService.updateSearchRanks(
                searchRankService.findSearchRanks(),
                searchRankService.findKeywordRanks()
        );
    }
}
