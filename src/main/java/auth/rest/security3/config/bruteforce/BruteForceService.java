package auth.rest.security3.config.bruteforce;

public interface BruteForceService {
    void registerLoginFailure(String username);
    void resetBruteCounter(String username);
    boolean isBruteForce(String username);
}
