package auth.rest.security3.service;

import auth.rest.security3.domain.AuthAttack;
import auth.rest.security3.repository.AttackRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AttackServiceImpl implements AttackService {
    private final AttackRepository attackRepository;

    public AttackServiceImpl(AttackRepository attackRepository) {
        this.attackRepository = attackRepository;
    }

    @Override
    public AuthAttack findByIp(String ip) {
        return attackRepository.findByIp(ip);
    }

    @Override
    public List<AuthAttack> findLast10Attempts(String ip) {
        return attackRepository.findRecentAttemptsByIp(ip, LocalDateTime.now().minus(10, ChronoUnit.MINUTES));
    }

    @Transactional
    @Override
    public void save(AuthAttack authAttack) {
        attackRepository.save(authAttack);
    }
}
