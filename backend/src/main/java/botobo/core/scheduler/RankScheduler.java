package botobo.core.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RankScheduler {

    private static final Logger log = LoggerFactory.getLogger(RankScheduler.class);

    @Scheduled(cron = "0 0 4 * * *")
    public void updateRanks() {

    }

    @Scheduled(cron = "0 0/10 * * * *")
    @CacheEvict("workbookRanks")
    public void updateWorkbookRanks() {
        log.info("cleared cache for workbook rankings request");
    }
}
