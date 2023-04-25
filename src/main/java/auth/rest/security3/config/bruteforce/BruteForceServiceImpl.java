package auth.rest.security3.config.bruteforce;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class BruteForceServiceImpl implements BruteForceService {
    public static HashMap<String, Integer> cache = new HashMap<>();
    private int maxFailures = 3;
    @Override
    public void registerLoginFailure(String username) {
        System.out.println("CACHE: " + username);
        if(!isBruteForce(username)) {
            cache.put(username, cache.get(username) == null ? 1 : cache.get(username) + 1);
        }
    }

    @Override
    public void resetBruteCounter(String username) {
        cache.put(username, 0);
    }

    @Override
    public boolean isBruteForce(String username) {
        return cache.size() == maxFailures;
    }
}
