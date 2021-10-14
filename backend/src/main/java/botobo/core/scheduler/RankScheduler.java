package botobo.core.scheduler;

import botobo.core.application.rank.SearchRankService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Profile({"local", "dev", "prod"})
@Component
public class RankScheduler {

    @Value("${spring.config.activate.on-profile}")
    private String profileName;

    @Value("${scheduler.rank.hostname:}")
    private String hostName;

    private final SearchRankService searchRankService;

    public RankScheduler(SearchRankService searchRankService) {
        this.searchRankService = searchRankService;
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void updateSearchRanks() {
        if (isLocalProfile() || isIntendedHostName()) {
            searchRankService.updateSearchRanks();
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
