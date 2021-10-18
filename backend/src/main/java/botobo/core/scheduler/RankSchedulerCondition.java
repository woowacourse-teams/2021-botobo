package botobo.core.scheduler;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class RankSchedulerCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return isLocalProfile(context) || isIntendedHostName(context);
    }

    private boolean isLocalProfile(ConditionContext context) {
        String profileName = context.getEnvironment().getProperty("spring.config.activate.on-profile");
        return "local".equals(profileName);
    }

    private boolean isIntendedHostName(ConditionContext context) {
        String profileHostName = context.getEnvironment().getProperty("scheduler.rank.hostname");
        if (Objects.isNull(profileHostName)) {
            return false;
        }
        try {
            String runningHostName = InetAddress.getLocalHost().getHostName();
            return profileHostName.equals(runningHostName);
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
