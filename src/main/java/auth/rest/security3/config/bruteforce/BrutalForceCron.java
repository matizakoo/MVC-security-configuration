package auth.rest.security3.config.bruteforce;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableScheduling
public class BrutalForceCron {
    @Scheduled(initialDelay = 1000, fixedDelay = 60 * 1000)
    public void resetCounter() {
        Map<String, Integer> cache = BruteForceServiceImpl.cache;
        if(BruteForceServiceImpl.cache.size() != 0)
            for(Map.Entry<String, Integer> map : cache.entrySet()) {
                if(map.getValue() > 0)
                    BruteForceServiceImpl.cache.put(map.getKey(), map.getValue() - 1);
                else
                    BruteForceServiceImpl.cache.remove(map.getKey());
            }
    }
}
