package auth.rest.security3.config.bruteforce;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class BrutalForceCron {
    @Scheduled(initialDelay = 1000, fixedDelay = 60 * 1000)
    public void resetCounter() {
        System.out.println("clear");
        BruteForceServiceImpl.cache.clear();
    }
}
