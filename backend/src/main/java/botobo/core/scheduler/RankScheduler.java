package botobo.core.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public class RankScheduler {

    @Scheduled(cron = "0 0 4 * * *")
    public void updateRanks() {

    }
}
