package auth.rest.security3.service;

import auth.rest.security3.domain.AuthAttack;

import java.util.List;

public interface AttackService {
    AuthAttack findByIp(String ip);

    List<AuthAttack> findLast10Attempts(String ip);
    void save(AuthAttack authAttack);
}
