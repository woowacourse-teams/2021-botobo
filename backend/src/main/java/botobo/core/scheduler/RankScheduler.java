package botobo.core.scheduler;

import botobo.core.application.WorkbookRankService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class RankScheduler {

    @Value("${spring.config.activate.on-profile}")
    private String profileName;

    @Value("${rank.scheduler.hostname:}")
    private String hostName;

    private final WorkbookRankService workbookRankService;

    public RankScheduler(WorkbookRankService workbookRankService) {
        this.workbookRankService = workbookRankService;
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void updateRanks() {

    }

    @Scheduled(cron = "0 0/10 * * * *")
    @CacheEvict("workbookRanks")
    public void updateWorkbookRanks() {
        if (isLocalProfile() || isIntendedHostName()) {
            workbookRankService.removeWorkbookRanksCache();
        }
    }

    private boolean isLocalProfile() {
        return "local".equals(profileName);
    }

    private boolean isIntendedHostName() {
        try {
            String runningHostName = InetAddress.getLocalHost().getHostName();
            return hostName.equals(runningHostName);
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
