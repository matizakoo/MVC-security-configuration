package auth.rest.security3.repository;

import auth.rest.security3.domain.AuthAttack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttackRepository extends JpaRepository<AuthAttack, Long> {
    AuthAttack findByIp(String ip);

    @Query("SELECT a FROM AuthAttack a WHERE a.ip = :ip AND a.attemptTime > :fromTime")
    List<AuthAttack> findRecentAttemptsByIp(String ip, LocalDateTime fromTime);

    AuthAttack save(AuthAttack attack);
}
