package ddip.me.ddipbe.domain.repository;

import ddip.me.ddipbe.domain.Permit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PermitRepository extends JpaRepository<Permit, Long> {
    @Query("SELECT p FROM Permit p JOIN FETCH p.event WHERE p.event.uuid = :uuid AND p.token = :token")
    Optional<Permit> findByEventUuidAndToken(UUID uuid, String token);  // TODO: 쿼리 최적화

    boolean existsByEventUuidAndToken(UUID uuid, String token);
}
